package com.ling.common.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

@Configuration(proxyBeanMethods = false)
public class VectorConfiguration {
  @Value("${ling.vec-store.index}")
  private String index;

  @Value("${ling.vec-store.prefix}")
  private String prefix;

  @Value("${ling.vec-store.init}")
  private Boolean init;

  @Bean
  public VectorStore VecStore(JedisPooled jedisPooled, EmbeddingModel embeddingModel) {
    return RedisVectorStore.builder(jedisPooled, embeddingModel)
        .indexName(index) // Optional: defaults to "spring-ai-index"
        .prefix(prefix) // Optional: defaults to "embedding:"
        .metadataFields( // Optional: define metadata fields for filtering
            RedisVectorStore.MetadataField.tag("country"),
            RedisVectorStore.MetadataField.numeric("year"))
        .initializeSchema(init) // Optional: defaults to false
        .batchingStrategy(
            new TokenCountBatchingStrategy()) // Optional: defaults to TokenCountBatchingStrategy
        .build();
  }
}
