package com.ling.chat.entity;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Component
public class RedisChatMemory implements ChatMemory {

  private final RedisTemplate<String, Message> redisTemplate;

  public RedisChatMemory(@Qualifier("chatMemoryTemplate") RedisTemplate<String, Message> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public void add(String conversationId, Message message) {
    redisTemplate.opsForValue().set(conversationId,message);
  }

  @Override
  public void clear(String conversationId) {
    redisTemplate.delete(conversationId);
  }

  @Override
  public List<Message> get(String conversationId, int lastN) {
    return redisTemplate.opsForList().rightPop(conversationId, lastN);
  }

  @Override
  public void add(String conversationId, List<Message> messages) {
    redisTemplate.opsForList().rightPushAll(conversationId,messages);
  }
}
