package org.springframework.cloud.admin.uaa.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author zhy
 * @date 2021/6/23 16:16
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVo {
    private Integer id;
    private String tenantId;
    private String userName;
    private String name;
    private Integer status;
    private Integer type;
    private String description;
}
