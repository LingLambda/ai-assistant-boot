package com.ling.chat.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.tool.DefaultToolCallingManager;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.resolution.SpringBeanToolCallbackResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

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
    return lingClientBuild(applicationContext).build();
  }

  @Bean
  public ChatClient.Builder lingClientBuild(
      ApplicationContext applicationContext) {
    OpenAiApi openAiApi = OpenAiApi.builder()
        .baseUrl(baseurl)
        .apiKey(apiKey)
        .build();
    OpenAiChatOptions openAiChatOptions = new OpenAiChatOptions();
    openAiChatOptions.setModel(chatModelName);
    openAiChatOptions.setMaxTokens(chatMaxTokens);
    openAiChatOptions.setTemperature(temperature);
    openAiChatOptions.setStreamOptions(new OpenAiApi.ChatCompletionRequest.StreamOptions(true));
    SpringBeanToolCallbackResolver resolver =
        SpringBeanToolCallbackResolver.builder()
            .applicationContext(new GenericApplicationContext(applicationContext))
            .build();
    ToolCallingManager toolCallingManager =
        DefaultToolCallingManager.builder()
            .toolCallbackResolver(resolver)
            .build();
    OpenAiChatModel chatModel =
        OpenAiChatModel.builder()
            .openAiApi(openAiApi)
            .defaultOptions(openAiChatOptions)
            .toolCallingManager(toolCallingManager)
            .observationRegistry(ObservationRegistry.NOOP)
            .build();
    return ChatClient.builder(chatModel);
  }
}
