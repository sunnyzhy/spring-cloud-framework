//package org.springframework.cloud.admin.gateway.service;
//
//import net.minidev.json.JSONUtil;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.Charset;
//
///**
// * @author zhy
// * @date 2021/11/17 14:11
// */
//@Component
//public class ServerAuthenticationEntryPointImpl implements ServerAuthenticationEntryPoint {
//    @Override
//    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
//        ServerHttpResponse response = exchange.getResponse();
//        response.setStatusCode(HttpStatus.OK);
//        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//        response.getHeaders().set("Access-Control-Allow-Origin", "*");
//        response.getHeaders().set("Cache-Control", "no-cache");
//        String body = "token error";
//        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
//        return response.writeWith(Mono.just(buffer));
//    }
//
//}
