package org.springframework.security.oauth.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth.model.Jks;
import org.springframework.security.oauth.service.AuthenticationProviderImpl;
import org.springframework.security.oauth.service.UserDetailsImpl;
import org.springframework.security.oauth.service.UserDetailsServiceImpl;
import org.springframework.security.oauth.service.UserTokenService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhouyi
 * @date 2021/1/12 15:07
 */
@EnableAuthorizationServer
@Configuration
@AllArgsConstructor
@Slf4j
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private final JdbcClientDetailsService jdbcClientDetailsService;
    private final AuthenticationProviderImpl authenticationProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenStore tokenStore;
    private final UserApprovalHandler userApprovalHandler;
    private final AuthorizationCodeServices codeServices;
    private final Jks jks;
    private final UserTokenService userTokenService;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService);
    }

    /**
     * ??????:
     * 1. tokenServices ??? accessTokenConverter ????????????????????????????????????????????? tokenServices ?????????????????????, ???????????? token ????????? KeyGenerators.secureRandom(20) ??????????????????
     * 2. AccessTokenConverter ????????? AuthorizationServerTokenServices ?????????
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager())
                .accessTokenConverter(accessTokenConverter())
                // ????????????userDetailsService??????????????? token ?????????????????????: o.s.s.o.provider.endpoint.TokenEndpoint  : Handling error: IllegalStateException, UserDetailsService is required.
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore)
                .userApprovalHandler(userApprovalHandler)
                .authorizationCodeServices(codeServices);
    }

    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        // ??????authenticationManager?????????????????????AuthenticationProviderImpl??????????????????????????????????????????????????????????????????DaoAuthenticationProvider???????????????"UserDetailsService returned null, which is an interface contract violation"
        ProviderManager authenticationManager = new ProviderManager(Arrays.asList(authenticationProvider));
        //???????????????????????????????????????TokenBasedRememberMeServices???????????????Credentials?????????UserDetailsService?????????UsernameNotFoundException
//        authenticationManager.setEraseCredentialsAfterAuthentication(false);
        return authenticationManager;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                final Map<String, Object> additionalInformation = new HashMap<>();
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getUserAuthentication().getPrincipal();
                String tokenId = UUID.randomUUID().toString().replaceAll("-", "");
                additionalInformation.put("token_id", tokenId);
                additionalInformation.put("user_id", userDetails.getId());
                additionalInformation.put("tenant_id", userDetails.getTenantId());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                return super.enhance(accessToken, authentication);
            }

            @Override
            protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                String token;
                try {
                    Map<String, Object> payloadMap = (Map<String, Object>) getAccessTokenConverter().convertAccessToken(accessToken, authentication);
                    token = userTokenService.createToken(payloadMap);
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                    throw new IllegalStateException("Cannot convert access token to JSON", ex);
                }
                return token;
            }
        };
        converter.setKeyPair(jks.getKeyPair());
        return converter;
    }
}
