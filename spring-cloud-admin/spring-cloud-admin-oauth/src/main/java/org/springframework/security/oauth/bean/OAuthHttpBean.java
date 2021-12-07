package org.springframework.security.oauth.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuthHttpBean {
    @Bean
    @ConfigurationProperties(prefix = "oauth.http")
    public AuthHttpConfig authHttpConfig() {
        return new AuthHttpConfig();
    }

    @Data
    public class AuthHttpConfig {
        private String[] permit;
        private String loginPage;
        private String loginProcessingUrl;
    }
}
