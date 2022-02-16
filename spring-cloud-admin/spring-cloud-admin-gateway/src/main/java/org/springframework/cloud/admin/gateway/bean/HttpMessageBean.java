package org.springframework.cloud.admin.gateway.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
@AllArgsConstructor
public class HttpMessageBean {
    private final ObjectMapper objectMapper;

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters((Collection) converters.orderedStream().collect(Collectors.toList()));
    }

    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
        return mappingJackson2HttpMessageConverter;
    }

    /**
     * 在 spring-cloud-gateway 中，获取 body 后重新创建 ServerRequest 时，org.springframework.core.io.buffer.LimitedDataBufferList 会判断接收的数据大小, 默认为 262144, 即 256KB
     * 如果超过 256KB, 就会抛出异常
     * <p>
     * 有两种解决方法:
     * 1. 配置 maxInMemorySize 为 >262144 的合适数值
     * 2. 配置 maxInMemorySize = -1, 不限制大小
     *
     * @return
     */
    @Bean
    public CodecConfigurer codecConfigurer() {
        CodecConfigurer codecConfigurer = new DefaultServerCodecConfigurer();
        codecConfigurer.defaultCodecs().maxInMemorySize(-1);
        return codecConfigurer;
    }
}
