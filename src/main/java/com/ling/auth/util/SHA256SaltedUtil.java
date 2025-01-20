package com.ling.auth.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Formatter;

public class SHA256SaltedUtil {
  /**
   * 使用 SHA-256 算法加盐后对字符串进行加密
   * @param input 输入的字符串
   * @param salt 盐值
   * @return 加密后的 SHA-256 哈希值
   * @throws NoSuchAlgorithmException
   */
  public static String sha256WithSalt(String input, String salt) throws NoSuchAlgorithmException {
    // 将输入字符串与盐值结合
    String saltedInput = input + salt;

    // 获取 SHA-256 MessageDigest 实例
    MessageDigest digest = MessageDigest.getInstance("SHA-256");

    // 更新 MessageDigest，传入盐值结合后的字节数组
    byte[] hash = digest.digest(saltedInput.getBytes());

    // 将字节数组转换为十六进制字符串
    return byteArrayToHex(hash);
  }
  /**
   * 将字节数组转换为十六进制字符串
   * @param byteArray 字节数组
   * @return 十六进制字符串
   */
  private static String byteArrayToHex(byte[] byteArray) {
    // 使用 Formatter 生成十六进制字符串
    try (Formatter formatter = new Formatter()) {
      for (byte b : byteArray) {
        formatter.format("%02x", b);
      }
      return formatter.toString();
    }
  }

  /**
   * 生成一个随机盐值
   * @return 随机盐值
   */
  public static String generateSalt(int length) {
    // 使用 SecureRandom 来生成一个安全的随机盐值
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[length];
    random.nextBytes(salt);
    return byteArrayToHex(salt);
  }
}
