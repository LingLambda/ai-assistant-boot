package com.ling.common.util;

import lombok.Data;

/**
 * 全局统一返回结果类
 */
@Data
public class Result<T> {

  private Integer code; // 返回码
  private String message; // 返回消息
  private T data; // 返回数据

  private Result() {
  } // 私有化构造函数

  // 单一数据构建方法
  private static <T> Result<T> build(T data) {
    Result<T> result = new Result<>();
    if (data != null) result.setData(data);
    return result;
  }

  // 根据 resultCode 构建
  public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
    Result<T> result = build(body);
    result.setCode(resultCodeEnum.getCode());
    result.setMessage(resultCodeEnum.getMessage());
    return result;
  }

  // 根据 code 和 message 构建
  public static <T> Result<T> build(Integer code, String message) {
    Result<T> result = build(null);
    result.setCode(code);
    result.setMessage(message);
    return result;
  }

  // 操作成功
  public static <T> Result<T> ok() {
    return ok(null);
  }

  // 操作成功, 带数据
  public static <T> Result<T> ok(T data) {
    return build(data, ResultCodeEnum.SUCCESS);
  }

  // 操作失败
  public static <T> Result<T> fail() {
    return fail(null);
  }

  // 操作失败, 带数据
  public static <T> Result<T> fail(T data) {
    return build(data, ResultCodeEnum.FAIL);
  }

  // 链式设置消息
  public Result<T> message(String msg) {
    this.setMessage(msg);
    return this;
  }

  // 链式设置返回码
  public Result<T> code(Integer code) {
    this.setCode(code);
    return this;
  }

  // 判断是否成功
  public boolean isOk() {
    return this.code != null && this.code.equals(ResultCodeEnum.SUCCESS.getCode());
  }
}
