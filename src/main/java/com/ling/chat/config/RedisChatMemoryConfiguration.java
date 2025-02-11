package com.ling.chat.config;

import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.security.Key;
import java.util.List;

/**
 * @author LingLambda
 * @since 2025/1/20 22:13
 */


//TODO: 由于Message类不支持序列化，暂缓Redis缓存
/*
@Configuration
public class RedisChatMemoryConfiguration {

  @Bean(name = "chatMemoryRedisConnectionFactory")
  public RedisConnectionFactory chatMemoryRedisConnectionFactory(){
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("1.1.11.137",6379);
    return new JedisConnectionFactory(redisStandaloneConfiguration);
  }

  @Bean
  public RedisTemplate<String, Message> redisTemplate(@Qualifier("chatMemoryRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Message> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    // 使用String序列化器序列化key
    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());

    // 使用Jackson序列化器序列化value
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

    template.afterPropertiesSet();
    return template;
  }
}
*/
