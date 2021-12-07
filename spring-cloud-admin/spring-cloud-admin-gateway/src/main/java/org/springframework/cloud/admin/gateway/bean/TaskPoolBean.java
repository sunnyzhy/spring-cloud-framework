package org.springframework.cloud.admin.gateway.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ForkJoinPool;

/**
 * @author zhy
 * @date 2021/12/6 18:05
 */
@Configuration
public class TaskPoolBean {
    @Bean(name = "forkJoinPool")
    public ForkJoinPool forkJoinPool() {
        int parallism = Runtime.getRuntime().availableProcessors();
        ForkJoinPool forkJoinPool = new ForkJoinPool(parallism);
        return forkJoinPool;
    }
}
