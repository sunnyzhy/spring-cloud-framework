package org.springframework.cloud.admin.uaa.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.cloud.admin.common.vo.ResponseTableVo;
import org.springframework.cloud.admin.uaa.model.BaseUser;
import org.springframework.cloud.admin.uaa.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author zhy
 * @date 2021/6/21 17:45
 */
@RestController
@RequestMapping(value = "/user",produces=MediaType.APPLICATION_JSON_VALUE )
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntityVo<BaseUser>> getUser(@PathVariable("id") Integer id) {
        return userService.getUser(id);
    }

    @GetMapping(value = "/all")
    public Mono<ResponseEntityVo<ResponseTableVo<BaseUser>>> getAllUser() {
        return userService.getAllUser();
    }
}
