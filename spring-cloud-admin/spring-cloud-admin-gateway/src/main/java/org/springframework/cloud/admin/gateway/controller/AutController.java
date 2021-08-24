package org.springframework.cloud.admin.gateway.controller;

import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.cloud.admin.gateway.service.AutService;
import org.springframework.cloud.admin.gateway.vo.AutVo;
import org.springframework.cloud.admin.gateway.vo.TokenVo;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author zhy
 * @date 2021/6/21 17:45
 */
@RestController
@RequestMapping(value = "/aut")
public class AutController {
    private final AutService autService;

    public AutController(AutService autService) {
        this.autService = autService;
    }

    @PostMapping(value = "/login")
    public Mono<ResponseEntityVo<TokenVo>> login(@RequestBody AutVo autVo) {
        return autService.login(autVo);
    }

    @GetMapping("/refresh")
    public Mono<ResponseEntityVo<TokenVo>> refreshToken(@RequestHeader(value = "access-token") String token) {
        return autService.refreshToken(token);
    }

}
