package org.springframework.cloud.admin.minio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SpringCloudAdminMinioApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAdminMinioApplication.class, args);
    }

}
