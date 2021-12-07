package org.springframework.security.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth.model.Jks;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhy
 * @date 2021/11/17 16:43
 */
@Service
@RequiredArgsConstructor
public class UserTokenService {
    @Value("${oauth.token.expiration:120}")
    private int tokenExpiration;
    private final ObjectMapper objectMapper;
    private final Jks jks;

    public String createToken(Map<String, Object> payloadMap) throws IllegalStateException {
        // header
        Map<String, Object> headers = new HashMap<>();
        headers.put("kid", jks.getVerifierKeyId());
        headers.put("test", "001");
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS512)
                .type(JOSEObjectType.JWT)
                .customParams(headers)
                .build();
        // payload
        long currentTime = System.currentTimeMillis();
        currentTime += tokenExpiration * 60 * 1000;
        // token 有效期
        payloadMap.put("exp", currentTime);
        String content;
        try {
            content = objectMapper.writeValueAsString(payloadMap);
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot convert access token to JSON", ex);
        }
        Payload payload = new Payload(content);
        // signature
        JWSSigner signer = new RSASSASigner(jks.getPrivateKey());
        // JWSObject
        JWSObject data = new JWSObject(header, payload);
        try {
            data.sign(signer);
        } catch (JOSEException ex) {
            throw new IllegalStateException("Cannot convert access token to JSON", ex);
        }

        String token = data.serialize();
        return token;
    }
}
