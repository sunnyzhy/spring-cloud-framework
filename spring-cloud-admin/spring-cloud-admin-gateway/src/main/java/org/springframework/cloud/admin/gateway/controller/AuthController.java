package org.springframework.cloud.admin.gateway.controller;

import lombok.AllArgsConstructor;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.cloud.admin.gateway.service.AuthService;
import org.springframework.cloud.admin.gateway.vo.AuthVo;
import org.springframework.cloud.admin.gateway.vo.TokenVo;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author zhy
 * @date 2021/6/21 17:45
 */
@RestController
@RequestMapping(value = "/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = "/login")
    public Mono<ResponseEntityVo<TokenVo>> login(@RequestBody AuthVo authVo) {
        return authService.login(authVo);
    }

    @GetMapping("/refresh")
    public Mono<ResponseEntityVo<TokenVo>> refreshToken(@RequestHeader(value = "access-token") String token) {
        return authService.refreshToken(token);
    }

}
