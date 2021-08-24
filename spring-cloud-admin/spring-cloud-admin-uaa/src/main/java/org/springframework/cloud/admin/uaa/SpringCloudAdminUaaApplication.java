package org.springframework.cloud.admin.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "org.springframework.cloud.admin.uaa.mapper")
public class SpringCloudAdminUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAdminUaaApplication.class, args);
    }

}
