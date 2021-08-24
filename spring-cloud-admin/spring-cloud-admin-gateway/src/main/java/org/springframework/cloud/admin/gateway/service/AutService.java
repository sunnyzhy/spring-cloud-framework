package org.springframework.cloud.admin.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.admin.common.to.AutTo;
import org.springframework.cloud.admin.common.to.UserTo;
import org.springframework.cloud.admin.common.utils.PasswordUtil;
import org.springframework.cloud.admin.common.utils.ResponseMonoUtil;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.cloud.admin.gateway.config.JwtConfig;
import org.springframework.cloud.admin.gateway.entity.TokenEntity;
import org.springframework.cloud.admin.gateway.feign.UaaFeign;
import org.springframework.cloud.admin.gateway.utils.JwtUtil;
import org.springframework.cloud.admin.gateway.vo.AutVo;
import org.springframework.cloud.admin.gateway.vo.TokenVo;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author zhy
 * @date 2021/6/24 14:33
 */
@Service
@Slf4j
public class AutService {
    private final UaaFeign uaaFeign;
    private final JwtConfig jwtConfig;
    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    public AutService(UaaFeign uaaFeign, JwtConfig jwtConfig, ReactiveRedisTemplate<String, Object> reactiveRedisTemplate) {
        this.uaaFeign = uaaFeign;
        this.jwtConfig = jwtConfig;
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    /**
     * 登录
     *
     * @param autVo
     * @return
     */
    public Mono<ResponseEntityVo<TokenVo>> login(AutVo autVo) {
        TokenVo tokenVo = new TokenVo();
        // 初始化AutTo
        AutTo autTo = new AutTo();
        autTo.setUserName(autVo.getUserName());
        try {
            Mono<ResponseEntityVo<UserTo>> mono = uaaFeign.aut(autTo);
            mono.subscribe(x -> {
                if (x.getCode() == HttpStatus.OK.value()) {
                    UserTo user = x.getData();
                    if (user == null) {
                        ResponseMonoUtil.ok(tokenVo);
                    }
                    if (!PasswordUtil.matches(autVo.getPassword(), user.getPassword())) {
                        ResponseMonoUtil.ok(tokenVo);
                    }
                    initTokenVo(tokenVo, user);
                }
            });
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
        boolean verified = JwtUtil.verifyToken(token, jwtConfig);
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
        TokenEntity token = JwtUtil.generateToken(user, jwtConfig);
        // 把token存入redis
        Mono<Long> mono = reactiveRedisTemplate.opsForValue().set(token.getTokenId(), token.getToken(), token.getExpireTime());
        mono.subscribe(System.out::println);
        // 初始化TokenVo
        tokenVo.setToken(token.getToken());
        tokenVo.setUserStatus(user.getStatus());
    }
}
