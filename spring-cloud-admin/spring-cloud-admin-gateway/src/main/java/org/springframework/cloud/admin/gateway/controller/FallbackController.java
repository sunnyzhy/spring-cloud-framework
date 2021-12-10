package org.springframework.cloud.admin.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.admin.common.utils.ResponseMonoUtil;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import reactor.core.publisher.Mono;

/**
 * @author zhy
 * @date 2021/6/22 13:46
 */
@RestController
@RequestMapping("/fallback")
@Slf4j
public class FallbackController {
    @GetMapping
    public Mono<ResponseEntityVo> fallback(ServerWebExchange exchange) {
        Exception exception = exchange.getAttribute(ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR);
        ServerWebExchange delegate = ((ServerWebExchangeDecorator) exchange).getDelegate();
        log.error("服务调用失败, url = {}", delegate.getRequest().getURI(), exception);

        return ResponseMonoUtil.error(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
