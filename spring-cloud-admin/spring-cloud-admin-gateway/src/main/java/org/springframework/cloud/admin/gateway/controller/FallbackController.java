package org.springframework.cloud.admin.gateway.controller;

import org.springframework.cloud.admin.common.utils.ResponseMonoUtil;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author zhy
 * @date 2021/6/22 13:46
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {
    @GetMapping
    public Mono<ResponseEntityVo> fallback() {
        return ResponseMonoUtil.error(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
