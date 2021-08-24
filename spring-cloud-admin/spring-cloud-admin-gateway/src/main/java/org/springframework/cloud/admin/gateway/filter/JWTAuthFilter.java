package org.springframework.cloud.admin.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.admin.common.utils.ResponseUtil;
import org.springframework.cloud.admin.gateway.config.JwtConfig;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.admin.gateway.utils.JwtUtil;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author zhy
 * @date 2021/7/2 10:29
 */
@Component
public class JWTAuthFilter implements GlobalFilter, Ordered {
    private final ObjectMapper objectMapper;
    private final JwtConfig jwtConfig;

    public JWTAuthFilter(ObjectMapper objectMapper, JwtConfig jwtConfig) {
        this.objectMapper = objectMapper;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest httpRequest = exchange.getRequest();
        String uri = httpRequest.getURI().getPath();
        System.out.println(uri);

        // 从上下文中获取 token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.isEmpty(token)) {
            return responseFailed(exchange.getResponse(), HttpStatus.UNAUTHORIZED);
        }

        // 验证 token 是否有效
        boolean verified = JwtUtil.verifyToken(token, jwtConfig);
        if (!verified) {
            return responseFailed(exchange.getResponse(), HttpStatus.FORBIDDEN);
        }

        // 判断权限
        


        // 如果以上的验证都通过，就执行 chain 上的其他业务流程
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> responseFailed(ServerHttpResponse response, HttpStatus httpStatus) {
        DataBuffer buffer = ResponseUtil.error(response, httpStatus, objectMapper);
        return response.writeWith(Mono.just(buffer));
    }
}
