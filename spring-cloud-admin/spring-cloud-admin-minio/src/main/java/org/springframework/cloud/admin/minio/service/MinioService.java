package org.springframework.cloud.admin.minio.service;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.admin.minio.bean.MinioBean;
import org.springframework.cloud.admin.minio.util.MinioPolicyUtil;
import org.springframework.cloud.admin.minio.vo.DeleteVo;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zhy
 * @date 2022/1/11 17:34
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {
    private final MinioBean.Minio minio;
    private final MinioClient minioClient;
    private String separator = "/";
    private String bucketName = "asiatrip";
    // 把文件信息缓存到内存里，key = hash(folder + filename); value = folder + filename; 方便删除
    // 实际项目中，可以把文件信息存储到数据库，删除的时候，前端只用传送要删除的 id 列表即可
    private ConcurrentMap<Integer, String> cache = new ConcurrentHashMap<>();

    public Mono<String> upload(FilePart file, String folder) {
        try {
            // 判断 名称为 asiatrip 的 bucket 是否存在
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                // 创建名称为 asiatrip 的 bucket
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                // 设置 bucket 的读写策略
                String config = MinioPolicyUtil.config("minioPolicy.json", bucketName);
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(config).build());
            } else {
                log.warn("Bucket 'asiatrip' already exists.");
            }
            String fileName = file.filename();
            if (!StringUtils.isEmpty(folder)) {
                // 去掉目录首端的 /
                while (folder.indexOf(separator) == 0) {
                    folder = folder.substring(1);
                }
                // 去掉目录尾端的 /
                while (folder.lastIndexOf(separator) == folder.length() - 1) {
                    folder = folder.substring(0, folder.lastIndexOf(separator));
                }
                fileName = folder + separator + fileName;
            }
            // 从 headers 里提取 Content-Type，否则默认为 application/octet-stream
            List<String> contentTypeList = file.headers().get("Content-Type");
            // 上传文件到 minio 服务器
            String finalFileName = fileName;
            Mono<String> mono = DataBufferUtils.join(file.content())
                    .map(dataBuffer -> {
                        try {
                            minioClient.putObject(
                                    PutObjectArgs.builder()
                                            .bucket(bucketName)
                                            .object(finalFileName)
                                            .contentType((contentTypeList == null || contentTypeList.isEmpty()) ? null : contentTypeList.get(0))
                                            .stream(dataBuffer.asInputStream(), dataBuffer.readableByteCount(), PutObjectArgs.MIN_MULTIPART_SIZE)
                                            .build());
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        // 前端访问 minio 服务器文件的 url
                        String url = minio.getEndpoint() + separator + bucketName + separator + finalFileName;
                        cache.put(finalFileName.hashCode(), finalFileName);
                        return url;
                    });
            return mono;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Mono.just(e.getMessage());
        }
    }

    public Mono<String> delete(DeleteVo entity) {
        if (entity.getFileNameList() == null || entity.getFileNameList().isEmpty()) {
            return Mono.just("ok");
        }
        try {
            List<DeleteObject> objects = new LinkedList<>();
            entity.getFileNameList().forEach(x -> {
                int hash = x.hashCode();
                if (cache.containsKey(hash)) {
                    objects.add(new DeleteObject(cache.get(hash)));
                }
            });
            Iterable<Result<DeleteError>> results =
                    minioClient.removeObjects(
                            RemoveObjectsArgs.builder().bucket(bucketName).objects(objects).build());
            // 一定用 for 迭代 results，否则删除操作不会执行
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                log.error("Error in deleting object " + error.objectName() + "; " + error.message());
            }
            return Mono.just("ok");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Mono.just(e.getMessage());
        }
    }
}
