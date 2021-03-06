package org.springframework.cloud.admin.gateway.feign;

import org.springframework.cloud.admin.common.to.LoginTo;
import org.springframework.cloud.admin.common.to.UserTo;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author zhy
 * @date 2021/6/24 14:19
 * <p>
 * 问题: block()/blockFirst()/blockLast() are blocking, which is not supported in thread reactor-http-nio-x
 * <p>
 * 原因:
 * 1. 由于 Spring Cloud Gateway 是基于 Spring 5、Spring Boot 2.X 和 Reactor 开发的响应式组件，运用了大量的异步实现。
 * 2. FeignClient 属于同步调用
 * <p>
 * 解决方法:
 * 同步转异步，如果需要获取返回结果，可以通过 Future 方式获取
 */
@FeignClient(value = "${feign.remote.application.uaa:spring-cloud-admin-uaa}")
public interface UaaFeign {
    @PostMapping(value = "/api/login")
    ResponseEntityVo<UserTo> login(@RequestBody LoginTo loginTo);
}
