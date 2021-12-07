package org.springframework.cloud.admin.gateway.bean;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author zhy
 * @date 2021/6/24 15:22
 * <p>
 * 问题: No qualifying bean of type 'org.springframework.boot.autoconfigure.http.HttpMessageConverters' available
 * <p>
 * 原因:
 * 1. 由于 Spring Cloud Gateway 是基于 Spring 5、Spring Boot 2.X 和 Reactor 开发的响应式组件，运用了大量的异步实现。
 * 2. 在项目启动过程中，并不会创建 HttpMessageConverters 实例。
 * <p>
 * 解决方法:
 * 需要手动创建相应的 Bean 注入到 Spring 容器
 */
@Configuration
public class FeignBean {
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters((Collection) converters.orderedStream().collect(Collectors.toList()));
    }
}
