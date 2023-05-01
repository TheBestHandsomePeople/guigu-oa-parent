package com.atguigu.common.result;

import lombok.Getter;

/**
 * @author bestHandsomePeople
 * @creat 2023-04-01-21:12
 */
@Getter
public enum  ResultCodeEnum {
    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    LOGIN_ERROR(205, "认证失败"),


    LOGIN_AUTH(208, "未登录"),
    PERMISSION(209, "没有权限");

    private Integer code;

    private String message;

   private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
