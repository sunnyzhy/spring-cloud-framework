package org.springframework.cloud.admin.minio.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.admin.minio.bean.MinioBean;
import org.springframework.cloud.admin.minio.util.MinioPolicyUtil;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author zhy
 * @date 2022/1/11 17:34
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {
    private final MinioBean.Minio minio;
    private String bucketName = "asiatrip";

    public Mono<String> upload(FilePart file) {
        try {
            // 创建 minioClient
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minio.getEndpoint())
                    .credentials(minio.getAccessKey(), minio.getSecretKey())
                    .build();

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
            // 从 headers 里提取 Content-Type，否则默认为 application/octet-stream
            List<String> contentTypeList = file.headers().get("Content-Type");
            // 上传文件到 minio 服务器
            Mono<String> mono = DataBufferUtils.join(file.content())
                    .map(dataBuffer -> {
                        try {
                            minioClient.putObject(
                                    PutObjectArgs.builder()
                                            .bucket(bucketName)
                                            .object(fileName)
                                            .contentType((contentTypeList == null || contentTypeList.isEmpty()) ? null : contentTypeList.get(0))
                                            .stream(dataBuffer.asInputStream(), dataBuffer.readableByteCount(), PutObjectArgs.MIN_MULTIPART_SIZE)
                                            .build());
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        // 前端访问 minio 服务器文件的 url
                        String url = minio.getEndpoint() + "/" + bucketName + "/" + fileName;
                        return url;
                    });
            return mono;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Mono.just(e.getMessage());
        }
    }
}
