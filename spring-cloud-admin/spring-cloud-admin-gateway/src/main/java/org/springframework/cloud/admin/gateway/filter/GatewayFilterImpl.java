package org.springframework.cloud.admin.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.admin.common.to.UserTo;
import org.springframework.cloud.admin.common.utils.ResponseUtil;
import org.springframework.cloud.admin.gateway.utils.HeaderUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.admin.gateway.utils.JwtUtil;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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

        // 修改 HTTP 请求的上下文信息
        ServerWebExchange exchange1 = resetServerWebExchange(exchange, user);
        if (exchange1 == null) {
            return responseFailed(exchange.getResponse(), HttpStatus.FORBIDDEN);
        }

        // 判断权限


        // 如果以上的验证都通过，就执行 chain 上的其他业务流程
        return resetFilter(exchange1, chain);
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

    /**
     * 修改 HTTP 请求的上下文信息
     * <p>
     * 主要用于修改/添加 ServerHttpRequest 的 headers(ServerHttpRequest.getHeaders() 是 ReadOnlyHttpHeaders 类型)
     * 所以需要用 build() 方法修改/添加自定义的 header
     *
     * @param exchange
     * @param user
     * @return
     */
    private ServerWebExchange resetServerWebExchange(ServerWebExchange exchange, UserTo user) {
        if (user == null) {
            return null;
        }
        ServerHttpRequest request = exchange.getRequest();
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("userId", user.getId().toString());
        headerMap.put("tenantId", user.getTenantId());
        headerMap.put("userHost", HeaderUtil.getRemoteIp(request));
        Consumer<HttpHeaders> headersConsumer = headers -> {
            headerMap.forEach((k, v) -> {
                headers.set(k, v);
            });
        };
        ServerHttpRequest request1 = request.mutate().headers(headersConsumer).build();
        return exchange.mutate().request(request1).build();
    }

    /**
     * 特殊处理 POST/PUT 请求
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> resetFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpMethod method = exchange.getRequest().getMethod();
        if (method == HttpMethod.POST || method == HttpMethod.PUT) {
            return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                String body = new String(bytes, StandardCharsets.UTF_8);
                exchange.getAttributes().put("POST_BODY", body);
                DataBufferUtils.release(dataBuffer);
                Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                    return Mono.just(buffer);
                });
                // 下面的将请求体再次封装写回到request里，传到下一级，否则，由于请求体已被消费，后续的服务将取不到值
                ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                    @Override
                    public Flux<DataBuffer> getBody() {
                        return cachedFlux;
                    }
                };
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            });
        } else {
            return chain.filter(exchange);
        }
    }
}
