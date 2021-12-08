package org.springframework.cloud.admin.gateway.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import sun.misc.BASE64Decoder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.stream.Collectors;

/**
 * @author zhy
 * @date 2021/11/29 14:14
 */
@Configuration
@Slf4j
public class PublicKeyBean {
    @Bean
    public RSAPublicKey publicKey() throws Exception {
        Resource resource = new ClassPathResource("public.key");
        InputStream inputStream = resource.getInputStream();
        String publicKeyData = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(publicKeyData);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        return rsaPublicKey;
    }
}
