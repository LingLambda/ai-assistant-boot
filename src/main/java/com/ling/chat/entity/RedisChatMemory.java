package com.ling.chat.entity;

// TODO: 由于Message类不支持序列化，暂缓Redis缓存

/*@Component
public class RedisChatMemory implements ChatMemory {

  private final RedisTemplate<String, Message> redisTemplate;

  public RedisChatMemory(RedisTemplate<String, Message> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public void add(String conversationId, Message message) {
    redisTemplate.opsForList().rightPush(conversationId,message);
  }

  @Override
  public void clear(String conversationId) {
    redisTemplate.delete(conversationId);
  }

  @Override
  public List<Message> get(String conversationId, int lastN) {
    // 获取列表中最后 N 个元素
    Long size = redisTemplate.opsForList().size(conversationId);
    if (size == null || size == 0) {
      return List.of();
    }
    long start = Math.max(size - lastN, 0);
    List<Message> range = redisTemplate.opsForList().range(conversationId, start, size - 1);
    System.out.println(range);
    return null;
  }

  @Override
  public void add(String conversationId, List<Message> messages) {
    redisTemplate.opsForList().rightPushAll(conversationId,messages);
  }
}*/
