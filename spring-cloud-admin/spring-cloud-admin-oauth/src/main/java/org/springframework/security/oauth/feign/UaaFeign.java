package org.springframework.security.oauth.feign;

import org.springframework.cloud.admin.common.to.LoginTo;
import org.springframework.cloud.admin.common.to.UserTo;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author zhy
 * @date 2021/6/24 14:19
 */
@FeignClient(value = "${feign.remote.application.uaa:spring-cloud-admin-uaa}")
public interface UaaFeign {
    @PostMapping(value = "/api/login")
    ResponseEntityVo<UserTo> login(@RequestBody LoginTo loginTo);
}
