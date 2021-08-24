package org.springframework.cloud.admin.gateway.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.admin.common.to.UserTo;
import org.springframework.cloud.admin.common.utils.UuidUtil;
import org.springframework.cloud.admin.gateway.config.JwtConfig;
import org.springframework.cloud.admin.gateway.constant.CLAIM_KEY;
import org.springframework.cloud.admin.gateway.entity.TokenEntity;

import java.util.Date;

/**
 * @author zhy
 * @date 2021/6/24 14:33
 */
@Slf4j
public class JwtUtil {
    private static final int MILLIS = 60 * 1000;
    private static final int SECONDS = 60;
    private static final String ISSUER = "issuer-zhy";

    /**
     * 生成token
     *
     * @param user
     * @param jwtConfig
     * @return
     */
    public static TokenEntity generateToken(UserTo user, JwtConfig jwtConfig) {
        TokenEntity tokenEntity = new TokenEntity();
        Algorithm algorithm = getAlgorithm(jwtConfig);
        if (algorithm == null) {
            return tokenEntity;
        }
        tokenEntity.setTokenId(UuidUtil.generate());
        tokenEntity.setExpireTime(jwtConfig.getExpireTime() * SECONDS);
        long timeStamp = System.currentTimeMillis();
        String token = JWT.create()
                .withIssuer(ISSUER) //签发人
                .withIssuedAt(new Date(timeStamp)) //签发时间
                .withExpiresAt(new Date(timeStamp + (jwtConfig.getExpireTime() * MILLIS))) // 过期时间
                .withClaim(CLAIM_KEY.TOKEN_ID.getName(), tokenEntity.getTokenId())
                .withClaim(CLAIM_KEY.USER_ID.getName(), user.getId())
                .withClaim(CLAIM_KEY.USER_NAME.getName(), user.getUserName())
                .withClaim(CLAIM_KEY.USER_TYPE.getName(), user.getType())
                .withClaim(CLAIM_KEY.USER_STATUS.getName(), user.getStatus())
                .withClaim(CLAIM_KEY.TENANT_ID.getName(), user.getTenantId())
                .sign(algorithm);
        tokenEntity.setToken(token);
        return tokenEntity;
    }

    /**
     * 验证token
     */
    public static boolean verifyToken(String token, JwtConfig jwtConfig) {
        Algorithm algorithm = getAlgorithm(jwtConfig);
        if (algorithm == null) {
            return false;
        }
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    /**
     * 根据密钥生成加密算法
     *
     * @param jwtConfig
     * @return
     */
    private static Algorithm getAlgorithm(JwtConfig jwtConfig) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey()); //算法
            return algorithm;
        } catch (IllegalArgumentException ex) {
            log.error(ex.getMessage());
            return null;
        }
    }

    public static UserTo getUser(String token) {
        UserTo user = new UserTo();
        DecodedJWT jwt = null;
        try {
            jwt = JWT.decode(token);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return user;
        }
        user.setId(jwt.getClaim(CLAIM_KEY.USER_ID.getName()).asInt());
        user.setUserName(jwt.getClaim(CLAIM_KEY.USER_NAME.getName()).asString());
        user.setType(jwt.getClaim(CLAIM_KEY.USER_TYPE.getName()).asInt());
        user.setStatus(jwt.getClaim(CLAIM_KEY.USER_STATUS.getName()).asInt());
        user.setTenantId(jwt.getClaim(CLAIM_KEY.TENANT_ID.getName()).asString());
        return user;
    }
}

