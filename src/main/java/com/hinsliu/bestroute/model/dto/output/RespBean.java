package com.hinsliu.bestroute.model.dto.output;

import com.hinsliu.bestroute.utils.enums.StatusCode;
import lombok.Builder;
import lombok.Data;

/**
 * @author: Hins Liu
 * @description: 请求响应体
 */
@Data
@Builder
public class RespBean<T> {

    private Boolean success;

    private Integer code;

    private String message;

    private T data;

    public static<T> RespBean<T> success(T data) {
        return new RespBeanBuilder<T>()
                .code(StatusCode.SUCCESS.getCode())
                .success(true)
                .message(StatusCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    public static<T> RespBean<T> fail(Integer code, String message) {
        return new RespBeanBuilder<T>()
                .code(code)
                .success(false)
                .message(message)
                .build();
    }

    public static<T> RespBean<T> fail() {
        return fail(StatusCode.FAIL.getCode(), StatusCode.FAIL.getMessage());
    }

    public static<T> RespBean<T> fail(String message) {
        return fail(StatusCode.FAIL.getCode(), message);
    }

    public static<T> RespBean<T> failNoPermission(String message) {
        return fail(StatusCode.UNAUTHENTICATED.getCode(), StatusCode.UNAUTHENTICATED.getMessage());
    }

}
