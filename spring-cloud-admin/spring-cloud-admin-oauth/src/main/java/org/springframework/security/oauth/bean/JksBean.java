package org.springframework.security.oauth.bean;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth.model.Jks;

@Configuration
public class JksBean {
    @Bean
    public Jks jks(){
        return new Jks("oauth2-auth-key", "oauth2.keystore", "zhy123");
    }

    @Bean
    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey
                .Builder(jks().getPublicKey())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS512);
        return new JWKSet(builder.build());
    }
}
