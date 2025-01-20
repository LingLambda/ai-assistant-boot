package com.ling.chat.entity;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class RedisChatMemory implements ChatMemory {



  @Override
  public void add(String conversationId, Message message) {
    ChatMemory.super.add(conversationId, message);
  }

  @Override
  public void clear(String conversationId) {

  }

  @Override
  public List<Message> get(String conversationId, int lastN) {
    return List.of();
  }

  @Override
  public void add(String conversationId, List<Message> messages) {

  }
}
