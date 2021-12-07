package org.springframework.cloud.admin.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhy
 * @date 2021/6/25 11:58
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {
    private int expireTime;
    private int refreshExpireTime;
}
