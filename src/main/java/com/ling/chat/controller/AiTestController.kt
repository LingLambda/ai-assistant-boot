package com.ling.chat.controller

import com.ling.chat.entity.SqlChatMemory
import com.ling.chat.service.RoomMessageService
import com.ling.chat.service.RoomService
import com.ling.chat.tools.DateTimeTools
import com.ling.common.config.CommonProperties
import com.ling.common.util.Result
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.api.Advisor
import org.springframework.ai.chat.memory.InMemoryChatMemory
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
    private val commonProperties: CommonProperties,
    private val roomService: RoomService
) {
    private var inMemoryChatMemory: InMemoryChatMemory? = null

    init {
        inMemoryChatMemory = InMemoryChatMemory()
    }

    @GetMapping("tools")
    fun chatTime(message: String): String? {
        val sqlChatMemory: Advisor =
            MessageChatMemoryAdvisor.builder(SqlChatMemory(roomMessageService))
                .conversationId("123321").build()
        return chatClient?.prompt(message)?.advisors(sqlChatMemory)?.call()
            ?.content()
    }

    @GetMapping("memory")
    fun memoryTest(
        message: String?,
        userId: Long?,
        conversationId: String?
    ): String? {
        checkNotNull(message)
        checkNotNull(userId)
        checkNotNull(conversationId)
        roomService.checkRoomExist(conversationId, userId)//检查房间，没有会创建房间
        val sqlChatMemory: Advisor =
            MessageChatMemoryAdvisor.builder(SqlChatMemory(roomMessageService))
                .conversationId(conversationId).build()
        return chatClient?.prompt(message)?.advisors(sqlChatMemory)
            ?.tools(DateTimeTools())?.call()?.content()
    }

    @GetMapping("remove")
    fun removeRoom(conversationId: String?): Result<Any> {
        checkNotNull(conversationId)
        roomService.removeRoom(conversationId)
        return Result.ok(null);
    }
}