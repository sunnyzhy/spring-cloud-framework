package org.springframework.security.oauth.bean;

import org.springframework.context.annotation.*;
import org.springframework.security.oauth2.provider.approval.*;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author zhouyi
 * @date 2021/1/11 14:56
 */
@Configuration
public class TokenStoreBean {
    @Resource
    private DataSource dataSource;

    /**
     * table: oauth_client_details
     * <p>
     * 如果 autoapprove 值是 true，就会直接返回授权码 code；否则，就会打开授权页面
     *
     * @return
     */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * table: oauth_approvals
     * <p>
     * 保存授权信息
     * 如果 oauth_approvals 表里保存了 userId 和 clientId 对应的授权信息，就会跳过授权页面而直接返回授权码 code
     * 可以结合 ApprovalStoreUserApprovalHandler 策略使用 ApprovalStore
     *
     * @return
     */
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * 该策略可以结合 ApprovalStore 使用
     *
     * @return
     */
    @Bean
    public UserApprovalHandler userApprovalHandler() {
        ApprovalStoreUserApprovalHandler userApprovalHandler = new ApprovalStoreUserApprovalHandler();
        userApprovalHandler.setApprovalStore(approvalStore());
        userApprovalHandler.setClientDetailsService(jdbcClientDetailsService());
        userApprovalHandler.setRequestFactory(new DefaultOAuth2RequestFactory(jdbcClientDetailsService()));
        return userApprovalHandler;
    }

    /**
     * table: oauth_code
     * <p>
     * 保存授权码 code
     *
     * @return
     */
    @Bean
    public AuthorizationCodeServices codeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * table: oauth_access_token, oauth_refresh_token
     * <p>
     * 保存 access_token 和 refresh_token
     * <p>
     * 注意：刷新 token 的时候
     * 1. 如果 refresh_token 没有失效，则只会重新生成 access_token；否则，会重新生成 access_token 和 refresh_token，并且 oauth_access_token.refresh_token = oauth_refresh_token.token_id
     * 2. 这一种刷新机制与 jwt 不同，jwt 模式下，每次刷新 token 都会重新生成 access_token 和 refresh_token
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }
}
