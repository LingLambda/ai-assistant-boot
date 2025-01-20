package com.ling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

/**
 * @author LingLambda
 * @since 2025/1/19 22:09
 */
@SpringBootApplication(exclude = RedisAutoConfiguration.class)
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class,args);
  }
}
