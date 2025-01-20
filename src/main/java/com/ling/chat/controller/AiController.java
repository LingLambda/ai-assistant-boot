package com.ling.chat.controller;

import com.ling.chat.entity.RedisChatMemory;
import com.ling.common.util.Result;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("chat")
@CrossOrigin
public class AiController {

  private final ChatClient chatClient;
  private final VectorStore vectorStore;
  private final RedisChatMemory redisChatMemory;
  private InMemoryChatMemory inMemoryChatMemory;

  AiController(
      ChatClient chatClient,
      VectorStore vectorStore, RedisChatMemory redisChatMemory) {
    this.chatClient = chatClient;
    this.vectorStore = vectorStore;
    this.redisChatMemory = redisChatMemory;
  }

  @GetMapping("weather")
  String chat(@RequestParam String city) {
    var userPromptTemplate = "现在{cityName}的天气怎么样？";
    return chatClient
        .prompt()
        .user(userSpec -> userSpec.text(userPromptTemplate).param("cityName", city))
        .functions("weatherByCity")
        .call()
        .content();
  }

  @GetMapping("book")
  String chatBook(@RequestParam String author) {
    var userPromptTemplate = "What books written by {author} are available in the library?";
    return chatClient
        .prompt()
        .user(userSpec -> userSpec.text(userPromptTemplate).param("author", author))
        .functions("booksByAuthor")
        .call()
        .content();
  }

  @GetMapping("vector")
  public ChatResponse vecChat(String message) {
    return chatClient
        .prompt()
        .advisors(new QuestionAnswerAdvisor(vectorStore))
        .user(message)
        .call()
        .chatResponse();
  }

  @PostMapping("vecChat")
  public Flux<ChatResponse> vecChat(@RequestBody List<Message> listMessage){
    ChatClient.ChatClientRequestSpec chatClientRequestSpec=null;
    if(listMessage==null|| listMessage.isEmpty()){
      return null;
    }else if(listMessage.size()==1){
      chatClientRequestSpec = initContext(listMessage.get(0));
    }else {
      chatClientRequestSpec = chatClient.prompt(new Prompt(listMessage)).advisors(getDefaultAdvisor());
    }
    return chatClientRequestSpec.stream().chatResponse();
  }
  @PostMapping("test")
  public Prompt test(){
    UserMessage userMessage = new UserMessage("用户消息");
    SystemMessage systemMessage = new SystemMessage("系统消息");
    System.out.println(userMessage);
    System.out.println(systemMessage);
    return new Prompt(List.of(userMessage,systemMessage));
  }
  @PostMapping("test2")
  public List<Message> test2(){
    UserMessage userMessage = new UserMessage("用户消息");
    SystemMessage systemMessage = new SystemMessage("系统消息");
    System.out.println(userMessage);
    System.out.println(systemMessage);
    return List.of(userMessage,systemMessage);
  }
  @PostMapping("test3")
  public List<Message> test3(@RequestBody String message){
    if(inMemoryChatMemory==null){
      inMemoryChatMemory = new InMemoryChatMemory();
    }
    inMemoryChatMemory.add("1", new UserMessage(message));
    System.out.println(inMemoryChatMemory);
    return inMemoryChatMemory.get("1",10);
  }
  @PostMapping("test4")
  public List<Message> test4(@RequestBody String message){
    redisChatMemory.add("1", new UserMessage(message));
    System.out.println(redisChatMemory);
    return redisChatMemory.get("1",10);
  }

  private ChatClient.ChatClientRequestSpec initContext(Message userMessage){

    Prompt initPrompt = new Prompt(new SystemMessage("你是一个人工智能研究生招生客服"));
    return chatClient.prompt(initPrompt).advisors(getDefaultAdvisor()).user(userMessage.getText());
  }

  private QuestionAnswerAdvisor getDefaultAdvisor(){
     return QuestionAnswerAdvisor.builder(vectorStore).userTextAdvise("""
             上下文信息如下 ---------------------
             
             ---------------------
             {question_answer_context}
             ---------------------
             
             根据上下文和提供的历史信息而不是先验知识
             回复用户问题。如果答案不在上下文中，请告知
             用户无法回答该问题。""").build();
  }


  /*
   * 上下文信息如下 ---------------------
   *
   * ---------------------
   * {question_answer_context}
   * ---------------------
   *
   * 根据上下文和提供的历史信息而不是先验知识
   * 回复用户问题。如果答案不在上下文中，请告知
   * 用户无法回答该问题。
   */
}
