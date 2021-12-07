package org.springframework.security.oauth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.cloud.admin.common.utils.ResponseMonoUtil;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author zhouyi
 * @date 2021/1/11 14:56
 */
@RestController
@Slf4j
@AllArgsConstructor
public class JksController {
    private final JWKSet jwkSet;

    @GetMapping(value = "/oauth/key")
    public Mono<ResponseEntityVo<JSONObject>> publicKey() {
        JSONObject publicKey = jwkSet.toJSONObject();
        return ResponseMonoUtil.ok(publicKey);
    }
}