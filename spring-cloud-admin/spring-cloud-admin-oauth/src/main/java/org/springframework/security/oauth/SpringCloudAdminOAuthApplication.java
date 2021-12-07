package org.springframework.security.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan(basePackages = "org.springframework.security.oauth.mapper")
public class SpringCloudAdminOAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAdminOAuthApplication.class, args);
    }

}
