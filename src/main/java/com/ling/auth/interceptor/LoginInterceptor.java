package com.ling.auth.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ling.auth.util.JwtUtil;
import com.ling.common.util.Result;
import com.ling.common.util.ResultCodeEnum;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * @author LingLambda
 * @since 2025/1/7 15:57
 */
public class LoginInterceptor implements HandlerInterceptor {

  private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler)
      throws IOException {
    String token = request.getHeader("Authorization");
    if (token == null || token.trim()
        .isEmpty()) {
      log.info("JWT validation failed: noToken");
      response.setContentType("application/json;charset=utf-8");
      response
          .getWriter()
          .print(getFailTokenJsonRes("No token", ResultCodeEnum.FETCH_TOKEN_FAILED));
      return false;
    }
    String failInfo = "Invalid token";
    try {
      Integer roleId = JwtUtil.getRoleIdFromToken(token);
      if (roleId != null) {
        if (isAdminPath(request.getRequestURI()) && !hasAdminPermission(roleId)) {
          response.setContentType("application/json;charset=utf-8");
          response.getWriter()
              .print(getFailTokenJsonRes(null, ResultCodeEnum.PERMISSION_DENIED));
          return false;
        }
        return true;
      }
    } catch (ExpiredJwtException e) {
      // 处理过期的 token
      failInfo = "Token has expired.";
    } catch (SignatureException e) {
      // 处理签名无效的 token
      failInfo = "Invalid token signature.";
    } catch (MalformedJwtException e) {
      // 处理格式错误的 token
      failInfo = "Malformed token.";
    } catch (Exception e) {
      // 处理其他可能的异常
      failInfo = "Token parsing error.";
      log.error("Unknown token parsing error{}", e.getMessage());
    }
    log.info("JWT validation failed: {}", failInfo);
    response.setContentType("application/json;charset=utf-8");
    response.getWriter()
        .print(getFailTokenJsonRes(failInfo, ResultCodeEnum.FETCH_TOKEN_FAILED));
    return false;
  }

  private String getFailTokenJsonRes(
      String failInfo, ResultCodeEnum resultCodeEnum)
      throws JsonProcessingException {
    Result<Object> build = Result.build(failInfo, resultCodeEnum);
    return objectMapper.writeValueAsString(build);
  }

  private boolean isAdminPath(String requestUri) {
    return requestUri.startsWith("/admin");
  }

  private boolean hasAdminPermission(Integer roleId) {
    return roleId >= 2;
  }
}
