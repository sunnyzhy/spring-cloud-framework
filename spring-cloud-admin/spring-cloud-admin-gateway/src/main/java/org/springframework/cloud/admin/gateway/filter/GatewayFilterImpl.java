package org.springframework.cloud.admin.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.admin.common.to.UserTo;
import org.springframework.cloud.admin.common.utils.ResponseUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.admin.gateway.utils.JwtUtil;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.interfaces.RSAPublicKey;

/**
 * @author zhy
 * @date 2021/7/2 10:29
 */
@Component
@AllArgsConstructor
public class GatewayFilterImpl implements GatewayFilter, Ordered {
    private final ObjectMapper objectMapper;
    private final RSAPublicKey publicKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest httpRequest = exchange.getRequest();
        String uri = httpRequest.getURI().getPath();

        // 从上下文中获取 token
        String token = httpRequest.getHeaders().getFirst("access-token");
        if (StringUtils.isEmpty(token)) {
            return responseFailed(exchange.getResponse(), HttpStatus.UNAUTHORIZED);
        }

        // 验证 token 是否有效
        boolean verified = JwtUtil.verifyToken(publicKey, token);
        if (!verified) {
            return responseFailed(exchange.getResponse(), HttpStatus.FORBIDDEN);
        }

        UserTo user = JwtUtil.getUser(token);
        if (user == null) {
            return responseFailed(exchange.getResponse(), HttpStatus.FORBIDDEN);
        }
        // 判断权限


        // 如果以上的验证都通过，就执行 chain 上的其他业务流程
        return chain.filter(exchange);
//        return chain.filter(exchange).then(Mono.fromRunnable(() ->
//        {
//            // 获取请求地址
//            String afterPath = httpRequest.getPath().pathWithinApplication().value();
//            // 获取响应状态码
//            HttpStatus afterStatusCode = exchange.getResponse().getStatusCode();
//            System.out.println("响应码：" + afterStatusCode + "，请求路径：" + afterPath);
//        }));
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
