package org.springframework.security.oauth.client.bean;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * @author zhy
 * @date 2022/1/5 18:20
 */
@Configuration
@EnableOAuth2Client
public class ClientBean {
    @ConfigurationProperties(prefix = "security.oauth2.client.client-auth-code")
    @Bean
    public OAuth2ProtectedResourceDetails authorizationCodeResourceDetails() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    public OAuth2RestTemplate authorizationCodeRestTemplate(
            @Qualifier("authorizationCodeResourceDetails") OAuth2ProtectedResourceDetails resourceDetails,
            OAuth2ClientContext oauth2ClientContext) {
        return new OAuth2RestTemplate(resourceDetails, oauth2ClientContext);
    }

}
