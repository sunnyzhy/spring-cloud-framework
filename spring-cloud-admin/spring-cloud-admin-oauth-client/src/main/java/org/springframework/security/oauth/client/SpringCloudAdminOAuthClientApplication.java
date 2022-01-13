package org.springframework.security.oauth.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author zhy
 * @date 2022/1/5 17:50
 */
@SpringBootApplication
@EnableEurekaClient
public class SpringCloudAdminOAuthClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAdminOAuthClientApplication.class, args);
    }
}
