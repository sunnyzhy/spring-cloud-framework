package org.springframework.cloud.admin.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.admin.common.to.UserTo;
import org.springframework.cloud.admin.common.utils.ResponseUtil;
import org.springframework.cloud.admin.gateway.utils.HeaderUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.admin.gateway.utils.JwtUtil;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
        HttpRequestContext context = setHttpRequestContext(exchange, user);
        if (context == null) {
            return responseFailed(exchange.getResponse(), HttpStatus.FORBIDDEN);
        }

        // 判断权限


        // 如果以上的验证都通过，就执行 chain 上的其他业务流程
        return chain.filter(context.getExchange());
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
     *
     * 主要用于修改/添加 ServerHttpRequest 的 headers(ServerHttpRequest.getHeaders() 是 ReadOnlyHttpHeaders 类型)
     * 所以需要用 build() 方法修改/添加自定义的 header
     *
     * @param exchange
     * @param user
     * @return
     */
    private HttpRequestContext setHttpRequestContext(ServerWebExchange exchange, UserTo user) {
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
        HttpRequestContext context = new HttpRequestContext();
        context.setRequest(request.mutate().headers(headersConsumer).build());
        context.setExchange(exchange.mutate().request(context.request).build());
        return context;
    }

    @Data
    class HttpRequestContext {
        private ServerHttpRequest request;
        private ServerWebExchange exchange;
    }

}
