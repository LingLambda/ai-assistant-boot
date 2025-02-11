package com.ling.chat.entity;

/**
 * @author LingLambda
 * @since 2025/1/21 00:03
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationRequest {
  private String conversationId;
  private String message;
}
