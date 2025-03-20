package com.ling.chat.service.impl

import com.ling.chat.service.ChatService
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class OpenAiChatService(
    private val chatClient: ChatClient,
    private val vectorStore: VectorStore,
) : ChatService {
    @Value("\${ling.max-memory:20}")
    var maxMemorySize: Int = 20

    override fun vectorSpec(
        systemMessage: String,
        message: String,
        chatMemory: ChatMemory,
        conversationId: String
    ): ChatClient.ChatClientRequestSpec =
        chatClient
            .prompt()
            .system(systemMessage)
            .user(message)
            .advisors(
                buildQuestionAnswerAdvisor(),
                buildMessageChatMemoryAdvisor(chatMemory, conversationId)
            )

    private fun buildMessageChatMemoryAdvisor(
        chatMemory: ChatMemory,
        conversationId: String
    ) = MessageChatMemoryAdvisor(chatMemory, conversationId, maxMemorySize)

    private fun buildQuestionAnswerAdvisor() =
        QuestionAnswerAdvisor.builder(vectorStore)
            .userTextAdvise(
                """
                你只有在必要时才使用下面的检索增强信息，检索增强信息每次都会提供给你，一般与90%的对话无关
                上下文信息如下 ---------------------
                ---------------------
                {question_answer_context}
                ---------------------
                你可以参考这些资料回答用户问题。
                """.trimIndent()
            )
            .build()
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