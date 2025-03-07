package com.ling.common.exception;

/**
 * @author LingLambda
 * @date 2025/1/510:33
 */
public class ChunkTextException extends Exception {

  public ChunkTextException(String message) {
    super("字符串切割错误!" + message);
  }

  public ChunkTextException() {
    super("字符串切割错误!");
  }
}
