package org.springframework.cloud.admin.gateway.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.admin.common.to.UserTo;
import org.springframework.cloud.admin.common.utils.UuidUtil;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.cloud.admin.gateway.constant.CLAIM_KEY;
import org.springframework.cloud.admin.gateway.entity.TokenEntity;
import org.springframework.cloud.admin.gateway.feign.OAuthFeign;
import org.springframework.http.HttpStatus;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

/**
 * @author zhy
 * @date 2021/6/24 14:33
 */
@Slf4j
public class JwtUtil {
    /**
     * 生成token
     *
     * @param user
     * @return
     */
    public static TokenEntity generateToken(OAuthFeign authFeign, UserTo user, ForkJoinPool forkJoinPool) {
        TokenEntity tokenEntity = new TokenEntity();
        String tokenId = UuidUtil.generate();
        tokenEntity.setTokenId(tokenId);
        final Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("token_id", tokenId);
        payloadMap.put("user_id", user.getId());
        payloadMap.put("tenant_id", user.getTenantId());
        try {
            CompletableFuture<ResponseEntityVo<String>> future = CompletableFuture.supplyAsync(() -> authFeign.token(payloadMap), forkJoinPool);
            ResponseEntityVo<String> responseEntityVo = future.get();
            if (responseEntityVo.getCode() == HttpStatus.OK.value()) {
                tokenEntity.setToken(responseEntityVo.getData());
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return tokenEntity;
    }

    /**
     * 验证token
     */
    public static boolean verifyToken(RSAPublicKey publicKey, String token) {
        if (publicKey == null) {
            return false;
        }
        try {
            JWSObject data = JWSObject.parse(token);
            JWSVerifier verifier = new RSASSAVerifier(publicKey);
            long expTime = data.getPayload().toJSONObject().getAsNumber("exp").longValue();
            long currentTime = System.currentTimeMillis();
            if (currentTime > expTime) {
                return false;
            }
            return data.verify(verifier);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    public static UserTo getUser(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            UserTo user = new UserTo();
            user.setId(claims.getIntegerClaim(CLAIM_KEY.USER_ID.getName()));
            user.setUserName(claims.getStringClaim(CLAIM_KEY.USER_NAME.getName()));
            user.setTenantId(claims.getStringClaim((CLAIM_KEY.TENANT_ID.getName())));
            return user;
        } catch (ParseException ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }
}

