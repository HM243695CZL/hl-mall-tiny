package com.macro.mall.tiny.common.api;


/**
 * 封装API的错误码
 */
public interface IErrorCode {
    long getStatus();

    String getMessage();
}
