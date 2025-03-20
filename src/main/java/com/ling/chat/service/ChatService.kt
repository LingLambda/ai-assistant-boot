package com.ling.chat.service

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.memory.ChatMemory

/**
 *@author LingLambda
 *@since 2025/3/19 09:23
 */
interface ChatService {

    /**
     * 调用向量数据库实现RAG请求
     * @param message 用户消息
     */
    fun vectorSpec(
        systemMessage: String,
        message: String,
        chatMemory: ChatMemory,
        conversationId: String
    ): ChatClient.ChatClientRequestSpec
}