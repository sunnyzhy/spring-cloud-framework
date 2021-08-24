package org.springframework.cloud.admin.common.utils;

import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

/**
 * @author zhy
 * @date 2021/6/24 10:19
 */
public class ResponseMonoUtil {
    public static Mono<ResponseEntityVo> ok() {
        ResponseEntityVo responseEntityVo = ResponseUtil.ok();
        return Mono.justOrEmpty(responseEntityVo);
    }

    public static <T> Mono<ResponseEntityVo<T>> ok(T data) {
        ResponseEntityVo<T> responseEntityVo = ResponseUtil.ok(data);
        return Mono.justOrEmpty(responseEntityVo);
    }

    public static Mono<ResponseEntityVo> error(HttpStatus status) {
        ResponseEntityVo responseEntityVo = ResponseUtil.error(status);
        return Mono.justOrEmpty(responseEntityVo);
    }

    public static Mono<ResponseEntityVo> error(int code, String msg) {
        ResponseEntityVo responseEntityVo = ResponseUtil.error(code, msg);
        return Mono.justOrEmpty(responseEntityVo);
    }

    public static <T> Mono<ResponseEntityVo<T>> error(HttpStatus status, T data) {
        ResponseEntityVo<T> responseEntityVo = ResponseUtil.error(status, data);
        return Mono.justOrEmpty(responseEntityVo);
    }

    public static <T> Mono<ResponseEntityVo<T>> error(int code, String msg, T data) {
        ResponseEntityVo<T> responseEntityVo = ResponseUtil.error(code, msg, data);
        return Mono.justOrEmpty(responseEntityVo);
    }

}
