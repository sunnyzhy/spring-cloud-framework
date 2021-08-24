package org.springframework.cloud.admin.gateway.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author zhy
 * @date 2021/6/25 17:32
 */
@Configuration
public class RedisConfig {
    /**
     * 注意:
     * 1. ReactiveRedisTemplate 的 Bean 名称不能命名为 redisTemplate，可以命名为 reactiveRedisTemplate
     * 2. 在 .\org\springframework\boot\spring-boot-autoconfigure\2.5.1\spring-boot-autoconfigure-2.5.1.jar!\org\springframework\boot\autoconfigure\data\redis\RedisAutoConfiguration.class 类里已经有名称为 redisTemplate 的 Bean 了
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(LettuceConnectionFactory connectionFactory) {
        // stringRedisSerializer
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // jsonRedisSerializer
        Jackson2JsonRedisSerializer jsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jsonRedisSerializer.setObjectMapper(om);

        RedisSerializationContext.SerializationPair<String> stringSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer);
        RedisSerializationContext.SerializationPair<Object> jsonSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(jsonRedisSerializer);

        RedisSerializationContext<String, Object> context = new RedisSerializationContext<String, Object>() {
            @Override
            public SerializationPair<String> getKeySerializationPair() {
                return stringSerializationPair;
            }

            @Override
            public SerializationPair<Object> getValueSerializationPair() {
                return jsonSerializationPair;
            }

            @Override
            public SerializationPair<String> getHashKeySerializationPair() {
                return stringSerializationPair;
            }

            @Override
            public SerializationPair<Object> getHashValueSerializationPair() {
                return jsonSerializationPair;
            }

            @Override
            public SerializationPair<String> getStringSerializationPair() {
                return stringSerializationPair;
            }
        };

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }
}
