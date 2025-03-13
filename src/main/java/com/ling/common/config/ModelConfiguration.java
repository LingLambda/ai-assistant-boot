package com.ling.common.config;

import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LingLambda
 * @since 2025/1/19 22:21
 */
@Configuration
public class ModelConfiguration {

  @Value("${ling.ai.baseurl}")
  private String baseurl;

  @Value("${ling.ai.api-key}")
  private String apiKey;

  @Value("${ling.ai.embedding.model}")
  private String embeddingModelName;

  @Bean
  public EmbeddingModel EmbeddingModel() {
    OpenAiApi openAiApi = OpenAiApi.builder().baseUrl(baseurl).apiKey(apiKey).build();
    OpenAiEmbeddingOptions embeddingOptions = new OpenAiEmbeddingOptions();
    embeddingOptions.setModel(embeddingModelName);
    return new OpenAiEmbeddingModel(openAiApi, MetadataMode.EMBED, embeddingOptions);
  }
}
