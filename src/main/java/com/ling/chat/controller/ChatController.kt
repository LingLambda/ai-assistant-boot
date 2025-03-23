package com.ling.chat.controller

import com.ling.auth.util.JwtUtil
import com.ling.chat.entity.SqlChatMemory
import com.ling.chat.service.ConversationSummaryService
import com.ling.chat.service.RoomMessageService
import com.ling.chat.service.RoomService
import com.ling.chat.service.impl.OpenAiChatService
import com.ling.common.util.Result
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

/**
 *@author LingLambda
 *@since 2025/3/20 11:33
 */
@RestController
@CrossOrigin
@RequestMapping("chat")
class ChatController(
    private val openAiChatService: OpenAiChatService,
    private val roomMessageService: RoomMessageService,
    private val roomService: RoomService,
    private val conversationSummaryService: ConversationSummaryService
) {
    private val log: Logger = LoggerFactory.getLogger(
        ChatController::class.java
    )

    @GetMapping(
        value = ["vec_chat"],
        produces = [MediaType.TEXT_EVENT_STREAM_VALUE]
    )
    fun vecChatStream(
        conversationId: String, message: String,
        @RequestHeader("Authorization") token: String//从jtw获取userId防止伪造
    ): Flux<ChatResponse> {
        val userId = JwtUtil.getUserIdFromToken(token)
        val memory =
            SqlChatMemory(roomMessageService, roomService, userId)
        return openAiChatService.vectorSpec(
            "你是一名人工智能客服",
            message,
            memory,
            conversationId
        ).stream().chatResponse()
    }

    @GetMapping("summary")
    fun summaryTitle(conversationId: String): Result<String> {
        val title = try {
            conversationSummaryService.summaryTitle(conversationId)
        } catch (e: Exception) {
            log.error(e.message)
            "新对话"
        }
        roomService.setTitle(conversationId, title)
        return Result.ok(title);
    }
}