package org.springframework.cloud.admin.gateway.entity;

import lombok.Data;

/**
 * @author zhy
 * @date 2021/6/25 16:49
 */
@Data
public class TokenEntity {
    private String tokenId;
    private String token;
    // 过期时间，单位是秒
    private long expireTime;
}
