package org.springframework.cloud.admin.minio.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhy
 * @date 2022/1/11 17:48
 */
@Configuration
public class MinioBean {
    @ConfigurationProperties(prefix = "minio")
    @Bean
    public Minio minio() {
        return new Minio();
    }

    @Data
    public class Minio {
        private String endpoint;
        private String accessKey = "minioadmin";
        private String secretKey = "minioadmin";
    }
}
