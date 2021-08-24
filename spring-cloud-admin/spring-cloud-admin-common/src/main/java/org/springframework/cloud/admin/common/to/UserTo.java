package org.springframework.cloud.admin.common.to;

import lombok.Data;

/**
 * @author zhy
 * @date 2021/6/23 16:16
 */
@Data
public class UserTo {
    private Integer id;
    private String tenantId;
    private String userName;
    private String password;
    private String name;
    private Integer status;
    private Integer type;
    private String description;
}
