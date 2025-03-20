package com.ling.chat.controller

import com.ling.auth.util.JwtUtil
import com.ling.chat.entity.SqlChatMemory
import com.ling.chat.service.RoomMessageService
import com.ling.chat.service.RoomService
import com.ling.chat.service.impl.OpenAiChatService
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
    private val roomService: RoomService
) {
    @GetMapping(
        value = ["vec_chat"],
        produces = [MediaType.TEXT_EVENT_STREAM_VALUE]
    )
    fun vecChatStream(
        @RequestParam(required = true) conversationId: String,
        @RequestParam(required = true) message: String,
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
}