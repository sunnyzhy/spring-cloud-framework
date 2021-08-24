package org.springframework.cloud.admin.uaa.controller;

import org.springframework.cloud.admin.common.to.AutTo;
import org.springframework.cloud.admin.common.to.UserTo;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.cloud.admin.uaa.service.ApiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author zhy
 * @date 2021/6/21 17:45
 */
@RestController
@RequestMapping(value = "/api")
public class ApiController {
    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * AUT = Authentication，认证
     * @param autTo
     * @return
     */
    @PostMapping(value = "/aut")
    public Mono<ResponseEntityVo<UserTo>> aut(@RequestBody AutTo autTo) {
        return apiService.aut(autTo);
    }

}
