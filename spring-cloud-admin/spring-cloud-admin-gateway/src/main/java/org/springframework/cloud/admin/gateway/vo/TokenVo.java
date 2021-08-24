package org.springframework.cloud.admin.gateway.vo;

import lombok.Data;

/**
 * @author zhy
 * @date 2021/6/25 18:07
 */
@Data
public class TokenVo {
    private String token;
    private Integer userStatus;
}
