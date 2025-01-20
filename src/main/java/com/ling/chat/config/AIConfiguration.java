package com.ling.chat.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.model.function.DefaultFunctionCallbackResolver;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfiguration {
  @Value("${ling.ai.baseurl}")
  private String baseurl;

  @Value("${ling.ai.api-key}")
  private String apiKey;

  @Value("${ling.ai.model}")
  private String chatModelName;

  @Value("${ling.ai.embedding.model}")
  private String embeddingModelName;

  @Value("${ling.ai.tokens}")
  private Integer chatMaxTokens;

  @Value("${ling.ai.temperature}")
  private Double temperature;

  @Bean
  public ChatClient lingClient(ApplicationContext applicationContext) {
    DefaultFunctionCallbackResolver functionCallbackResolver =
        new DefaultFunctionCallbackResolver();
    functionCallbackResolver.setApplicationContext(applicationContext);
    OpenAiApi openAiApi = new OpenAiApi(baseurl, apiKey);
    OpenAiChatOptions openAiChatOptions = new OpenAiChatOptions();
    openAiChatOptions.setModel(chatModelName);
    openAiChatOptions.setMaxTokens(chatMaxTokens);
    openAiChatOptions.setTemperature(temperature);
    openAiChatOptions.setStreamOptions(new OpenAiApi.ChatCompletionRequest.StreamOptions(true));
    OpenAiChatModel chatModel =
        new OpenAiChatModel(
            openAiApi,
            openAiChatOptions,
            functionCallbackResolver,
            RetryUtils.DEFAULT_RETRY_TEMPLATE);
    return ChatClient.builder(chatModel).build();
  }
}
