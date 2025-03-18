package com.ling.chat.controller

import com.ling.chat.entity.SqlChatMemory
import com.ling.chat.service.RoomMessageService
import com.ling.chat.tools.*
import com.ling.common.config.CommonProperties
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.api.Advisor
import org.springframework.ai.chat.memory.InMemoryChatMemory
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.web.bind.annotation.*

/**
 *@author LingLambda
 *@since 2025/3/14 15:33
 */
@RestController
@RequestMapping("chat/test")
@CrossOrigin
class AiTestController(
    private var chatClient: ChatClient?,
    private var chatClientBuild: ChatClient.Builder?,
    private var vectorStore: VectorStore?,
    private val roomMessageService: RoomMessageService,
    private val commonProperties: CommonProperties
) {
    private var inMemoryChatMemory: InMemoryChatMemory? = null

    init {
        inMemoryChatMemory = InMemoryChatMemory()
    }

    @GetMapping("tools")
    fun chatTime(message: String): String? {
        val sqlChatMemory: Advisor =
            MessageChatMemoryAdvisor.builder(SqlChatMemory(roomMessageService)).conversationId("123321").build()
        return chatClient?.prompt(message)?.advisors(sqlChatMemory)?.call()?.content()
    }

    @GetMapping()
    fun memoryTest(message: String): String? {
        val sqlChatMemory: Advisor =
            MessageChatMemoryAdvisor.builder(SqlChatMemory(roomMessageService)).conversationId("123321").build()
        return chatClient?.prompt(message)?.advisors(sqlChatMemory)?.tools(
            DateTimeTools(), WeatherTools(), BookTools(), SearchTools(commonProperties)
        )?.call()?.content()
    }

    @GetMapping("clear")
    fun clearMemory(conversationId: String?) {

    }
}