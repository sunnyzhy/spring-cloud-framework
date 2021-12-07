package org.springframework.cloud.admin.uaa.controller;

import lombok.AllArgsConstructor;
import org.springframework.cloud.admin.common.to.LoginTo;
import org.springframework.cloud.admin.common.to.UserTo;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.cloud.admin.uaa.service.ApiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhy
 * @date 2021/6/21 17:45
 */
@RestController
@RequestMapping(value = "/api")
@AllArgsConstructor
public class ApiController {
    private final ApiService apiService;

    /**
     * AUT = Authentication，认证
     * @param loginTo
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseEntityVo<UserTo> login(@RequestBody LoginTo loginTo) {
        return apiService.login(loginTo);
    }

}
