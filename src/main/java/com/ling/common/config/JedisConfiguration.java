package com.ling.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

/**
 * @author LingLambda
 * @since 2025/1/19 22:18
 */
@Configuration
public class JedisConfiguration {

  @Value("${spring.data.redis.username}")
  private String username;
  @Value("${spring.data.redis.password}")
  private String password;
  @Value("${spring.data.redis.host}")
  private String host;
  @Value("${spring.data.redis.port}")
  private int port;

  @Bean
  public JedisPooled jedisPooled() {
    return new JedisPooled(host, port, username, password);
  }
}
