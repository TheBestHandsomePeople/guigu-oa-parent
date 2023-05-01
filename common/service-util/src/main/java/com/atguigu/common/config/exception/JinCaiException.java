package com.atguigu.common.config.exception;

import com.atguigu.common.result.ResultCodeEnum;
import lombok.Data;

/**
 * @author bestHandsomePeople
 * @creat 2023-04-03-16:40
 */
@Data
public class JinCaiException extends RuntimeException {

    private Integer code;

    private String message;

    public JinCaiException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public JinCaiException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }



}
