package org.springframework.cloud.admin.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author zhy
 * @date 2021/7/5 16:45
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseEntityVo<T> {
    private int code;
    private String msg;
    private T data;
}
