package com.ling.chat.controller;

import com.ling.chat.entity.ConversationRequest;
import com.ling.chat.tools.BookTools;
import com.ling.chat.tools.DateTimeTools;
import com.ling.chat.tools.WeatherTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("chat")
@CrossOrigin
public class AiController {

  private final ChatClient chatClient;
  private final ChatClient.Builder chatClientBuild;
  private final VectorStore vectorStore;
  private final InMemoryChatMemory inMemoryChatMemory;

  AiController(ChatClient chatClient, ChatClient.Builder chatClientBuild, VectorStore vectorStore) {
    this.chatClient = chatClient;
    this.chatClientBuild = chatClientBuild;
    this.vectorStore = vectorStore;
    inMemoryChatMemory = new InMemoryChatMemory();
  }

  @GetMapping("weather")
  String chat(@RequestParam String city) {
    var userPromptTemplate = "现在{cityName}的天气怎么样？";
    return chatClient
        .prompt()
        .user(userSpec -> userSpec.text(userPromptTemplate).param("cityName", city))
        .tools("weatherByCity")
        .call()
        .content();
  }

  @GetMapping("time")
  String chatTime() {
    var userPromptTemplate = "现在几点了，我的时区是哪里，北京天气咋样";
    return chatClient
        .prompt(userPromptTemplate)
        .tools(new DateTimeTools(), new WeatherTools())
        .call()
        .content();
  }

  @GetMapping("book")
  String chatBook(@RequestParam String author) {
    var userPromptTemplate = "What books written by {author} are available in the library?";
    return chatClient
        .prompt()
        .user(userSpec -> userSpec.text(userPromptTemplate).param("author", author))
        .tools(new BookTools())
        .call()
        .content();
  }

  @GetMapping("vector")
  public ChatResponse vecChat(String message) {
    return chatClient
        .prompt()
        .user(message)
        .advisors(new QuestionAnswerAdvisor(vectorStore))
        .user(message)
        .call()
        .chatResponse();
  }

  @GetMapping(value = "vec_chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ChatResponse> vecChat(String conversationId, String message) {
    ConversationRequest cr = new ConversationRequest(conversationId, message);
    if (cr.getConversationId() == null
        || cr.getConversationId().isEmpty()
        || cr.getMessage() == null
        || cr.getMessage().isEmpty()) {
      return Flux.empty();
    }
    ChatClient.ChatClientRequestSpec chatClientRequestSpec =
        chatClient
            .prompt()
            .system("你是一名人工智能客服")
            .user(cr.getMessage())
            .advisors(
                MessageChatMemoryAdvisor.builder(inMemoryChatMemory)
                    .conversationId(cr.getConversationId())
                    .build(),
                getDefaultAdvisor());
    System.out.println(inMemoryChatMemory.get(cr.getConversationId(), 10));
    return chatClientRequestSpec.stream().chatResponse();
  }

  private QuestionAnswerAdvisor getDefaultAdvisor() {
    return QuestionAnswerAdvisor.builder(vectorStore)
        .userTextAdvise(
            """
             你只有在必要时才使用下面的检索增强信息，检索增强信息每次都会提供给你，一般与90%的对话无关
             上下文信息如下 ---------------------
             ---------------------
             {question_answer_context}
             ---------------------
             你可以参考这些资料回答用户问题。
             """)
        .build();
    /*
             """
              上下文信息如下 ---------------------
              ---------------------
              {question_answer_context}
              ---------------------
              根据上下文和提供的历史信息而不是先验知识
              回复用户问题。如果答案不在上下文中，请告知
              用户不知道答案。如果用户的问题与上下文无关，
              请忽略上下文直接回答用户问题。
              """
    */
  }
}
