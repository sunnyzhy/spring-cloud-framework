package org.springframework.cloud.admin.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;

/**
 * @author zhy
 * @date 2021/6/24 10:19
 */
public class ResponseUtil {
    public static ResponseEntityVo ok() {
        ResponseEntityVo responseEntityVo = ResponseUtil.getResponseEntityVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        return responseEntityVo;
    }

    public static <T> ResponseEntityVo<T> ok(T data) {
        ResponseEntityVo<T> responseEntityVo = ResponseUtil.getResponseEntityVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
        return responseEntityVo;
    }

    public static ResponseEntityVo error(HttpStatus status) {
        ResponseEntityVo responseEntityVo = ResponseUtil.getResponseEntityVo(status.value(), status.getReasonPhrase());
        return responseEntityVo;
    }

    public static <T> ResponseEntityVo<T> error(int code, String msg) {
        ResponseEntityVo responseEntityVo = ResponseUtil.getResponseEntityVo(code, msg);
        return responseEntityVo;
    }

    public static <T> ResponseEntityVo<T> error(HttpStatus status, T data) {
        ResponseEntityVo<T> responseEntityVo = ResponseUtil.getResponseEntityVo(status.value(), status.getReasonPhrase(), data);
        return responseEntityVo;
    }

    public static <T> ResponseEntityVo<T> error(int code, String msg, T data) {
        ResponseEntityVo<T> responseEntityVo = ResponseUtil.getResponseEntityVo(code, msg, data);
        return responseEntityVo;
    }

    private static ResponseEntityVo getResponseEntityVo(int code, String msg) {
        ResponseEntityVo responseEntityVo = new ResponseEntityVo();
        responseEntityVo.setCode(code);
        responseEntityVo.setMsg(msg);
        return responseEntityVo;
    }

    private static <T> ResponseEntityVo<T> getResponseEntityVo(int code, String msg, T data) {
        ResponseEntityVo<T> responseEntityVo = new ResponseEntityVo<T>();
        responseEntityVo.setCode(code);
        responseEntityVo.setMsg(msg);
        responseEntityVo.setData(data);
        return responseEntityVo;
    }

    public static DataBuffer error(ServerHttpResponse httpResponse, HttpStatus httpStatus, ObjectMapper objectMapper) {
        httpResponse.setStatusCode(HttpStatus.OK);
        httpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        ResponseEntityVo responseEntityVo = error(httpStatus);
        byte[] responseByte = new byte[0];
        try {
            responseByte = objectMapper.writeValueAsBytes(responseEntityVo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        DataBuffer buffer = httpResponse.bufferFactory().wrap(responseByte);
        return buffer;
    }
}
