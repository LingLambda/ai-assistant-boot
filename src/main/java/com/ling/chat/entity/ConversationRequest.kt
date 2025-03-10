package com.ling.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LingLambda
 * @since 2025/1/21 00:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationRequest {
  private String conversationId;
  private String message;
}
