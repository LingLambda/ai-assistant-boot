package com.ling.chat.entity

import com.ling.chat.service.RoomMessageService
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.messages.Message

/**
 *@author LingLambda
 *@since 2025/3/13 17:48
 */
class SqlChatMemory(private val roomMessageService: RoomMessageService) :
    ChatMemory {

    override fun add(conversationId: String?, messages: MutableList<Message>?) {
        roomMessageService.addMessage(conversationId, messages)
    }

    override fun get(
        conversationId: String?,
        lastN: Int
    ): MutableList<Message> {
        return roomMessageService.getMessage(conversationId, lastN)
    }

    override fun clear(conversationId: String?) {
        roomMessageService.clearMessage(conversationId)
    }
}