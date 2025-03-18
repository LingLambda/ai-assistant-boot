package com.ling.common.util;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 */
@Getter
public enum ResultCodeEnum {
  SUCCESS(200, "成功"),
  FAIL(201, "失败"),
  PARAM_ERROR(202, "参数不正确"),
  SERVICE_ERROR(203, "服务异常"),
  DATA_ERROR(204, "数据异常"),
  DATA_UPDATE_ERROR(205, "数据版本异常"),

  CODE_ERROR(210, "验证码错误"),
  LOGIN_AURH(214, "需要登录"),
  LOGIN_ACL(215, "没有权限"),

  URL_ENCODE_ERROR(216, "URL编码失败"),
  ILLEGAL_CALLBACK_REQUEST_ERROR(217, "非法回调请求"),

  FETCH_TOKEN_FAILED(401, "获取令牌失败"),
  FETCH_USERINFO_ERROR(401, "获取用户信息失败"),
  LOGIN_ERROR(401, "登录失败"), // 401 未授权（Unauthorized）
  LOGIN_AUTH(401, "未登陆"), // 401 未授权（Unauthorized）
  INVALID_CREDENTIALS(403, "无效凭证"), // 403 禁止访问（Forbidden）
  PERMISSION_DENIED(403, "没有权限"), // 403 禁止访问（Forbidden）
  RESOURCE_NOT_FOUND(404, "资源未找到"), // 404 资源未找到（Not Found）

  FILE_IO_ERROR(1, "文件读取错误"),
  FILE_CHUNK_ERROR(2, "文件切分错误"),
  FILE_UPLOAD_ERROR(3, "文件上传失败"),
  FILE_DELETE_ERROR(4, "文件删除失败");

  private final Integer code;
  private final String message;

  ResultCodeEnum(Integer code, String message) {
    this.code = code;
    this.message = message;
  }
}
