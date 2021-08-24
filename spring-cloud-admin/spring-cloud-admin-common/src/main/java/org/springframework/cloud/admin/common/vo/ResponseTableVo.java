package org.springframework.cloud.admin.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author zhy
 * @date 2021/7/5 16:45
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseTableVo<T> {
    private long count;
    private List<T> rows;
}
