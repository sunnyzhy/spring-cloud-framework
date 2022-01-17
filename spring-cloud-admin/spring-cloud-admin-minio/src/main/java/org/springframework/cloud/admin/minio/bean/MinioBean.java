package org.springframework.cloud.admin.minio.bean;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhy
 * @date 2022/1/11 17:48
 */
@Configuration
public class MinioBean {
    private String separator = "/";

    @ConfigurationProperties(prefix = "minio")
    @Bean(name = "minio")
    public Minio minio() {
        return new Minio();
    }

    @Bean
    public MinioClient minioClient(@Qualifier(value = "minio") Minio minio) {
        String endpoint = minio.getEndpoint();
        // 去掉 endpoint 尾端的 /
        while (endpoint.lastIndexOf(separator) == endpoint.length() - 1) {
            endpoint = endpoint.substring(0, endpoint.lastIndexOf(separator));
        }
        minio.setEndpoint(endpoint);
        // 创建 minioClient
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(minio.accessKey, minio.secretKey)
                .build();
    }

    @Data
    public class Minio {
        private String endpoint;
        private String accessKey = "minioadmin";
        private String secretKey = "minioadmin";
    }
}
