package com.hinsliu.bestroute.utils.enums;

import lombok.Data;

/**
 * @author: Hins Liu
 * @description: 请求返回状态
 */
public enum StatusCode {

    SUCCESS(0, "请求成功"),

    FAIL(1, "请求失败"),

    UNAUTHENTICATED(2, "没有权限")

    ;

    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    StatusCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
