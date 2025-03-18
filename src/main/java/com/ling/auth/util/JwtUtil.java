package com.ling.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  // token有效时间72小时
  private static final long EXP = TimeUnit.HOURS.toMillis(72);
  private static byte[] keyByte;

  public JwtUtil(@Value("${ling.jwt.token}") String secretKey) {
    keyByte = secretKey.getBytes(StandardCharsets.UTF_8); // 通过构造器赋值给静态字段
  }

  // 生成 JWT
  public static String generateJwt(String username, Integer roleId, String roleName)
      throws NoSuchAlgorithmException {
    SecretKey key = Keys.hmacShaKeyFor(keyByte);

    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);
    long expMillis = nowMillis + EXP;
    Date exp = new Date(expMillis);

    return Jwts.builder()
        .subject(username) // 主题，可以是用户 ID 或其他标识符
        .claims(Map.of("roleId", roleId, "roleName", roleName))
        .issuedAt(now) // 签发时间
        .expiration(exp) // 过期时间
        .signWith(key)
        .compact();
  }

  // 解析 JWT
  private static Claims parseJwt(String token) {
    SecretKey key = Keys.hmacShaKeyFor(keyByte);
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }

  // 获取 JWT 的权限 id
  public static Integer getRoleIdFromToken(String token) {
    Claims claims = parseJwt(token);
    return claims.get("roleId", Integer.class);
  }

  public static Long getUserIdFromToken(String token) {
    Claims claims = parseJwt(token);
    return claims.get("userId", Long.class);
  }
}
