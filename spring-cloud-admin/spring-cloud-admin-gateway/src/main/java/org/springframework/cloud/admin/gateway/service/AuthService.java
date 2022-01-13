package org.springframework.cloud.admin.gateway.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.admin.common.to.LoginTo;
import org.springframework.cloud.admin.common.to.UserTo;
import org.springframework.cloud.admin.common.utils.ResponseMonoUtil;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.cloud.admin.gateway.entity.TokenEntity;
import org.springframework.cloud.admin.gateway.feign.OAuthFeign;
import org.springframework.cloud.admin.gateway.feign.UaaFeign;
import org.springframework.cloud.admin.gateway.utils.JwtUtil;
import org.springframework.cloud.admin.gateway.vo.AuthVo;
import org.springframework.cloud.admin.gateway.vo.TokenVo;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

/**
 * @author zhy
 * @date 2021/6/24 14:33
 */
@Service
@Slf4j
@AllArgsConstructor
public class AuthService {
    private final UaaFeign uaaFeign;
    private final OAuthFeign authFeign;
    private final RSAPublicKey publicKey;
    private final ForkJoinPool forkJoinPool;
    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    /**
     * 登录
     *
     * @param authVo
     * @return
     */
    public Mono<ResponseEntityVo<TokenVo>> login(AuthVo authVo) {
        TokenVo tokenVo = new TokenVo();
        // 初始化AutTo
        LoginTo loginTo = new LoginTo();
        loginTo.setUserName(authVo.getUserName());
        try {
            CompletableFuture<ResponseEntityVo<UserTo>> future = CompletableFuture.supplyAsync(() -> uaaFeign.login(loginTo), forkJoinPool);
            ResponseEntityVo<UserTo> responseEntityVo = future.get();
            if (responseEntityVo.getCode() == HttpStatus.OK.value()) {
                UserTo user = responseEntityVo.getData();
                TokenEntity tokenEntity = JwtUtil.generateToken(authFeign, user, forkJoinPool);
                tokenVo.setToken(tokenEntity.getToken());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseMonoUtil.ok(tokenVo);
    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    public Mono<ResponseEntityVo<TokenVo>> refreshToken(String token) {
        boolean verified = JwtUtil.verifyToken(publicKey, token);
        if (!verified) {
            return ResponseMonoUtil.error(HttpStatus.UNAUTHORIZED, null);
        }
        TokenVo tokenVo = new TokenVo();
        UserTo user = JwtUtil.getUser(token);
        initTokenVo(tokenVo, user);
        return ResponseMonoUtil.ok(tokenVo);
    }

    /**
     * 初始化TokenVo
     *
     * @param tokenVo
     * @param user
     */
    private void initTokenVo(TokenVo tokenVo, UserTo user) {
        // 生成token
        TokenEntity token = JwtUtil.generateToken(authFeign, user, forkJoinPool);
        // 把token存入redis
        Mono<Long> mono = reactiveRedisTemplate.opsForValue().set(token.getTokenId(), token.getToken(), token.getExpireTime());
        mono.subscribe(System.out::println);
        // 初始化TokenVo
        tokenVo.setToken(token.getToken());
    }
}
