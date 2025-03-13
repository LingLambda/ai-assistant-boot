package com.ling.chat.entity

import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.messages.Message

/**
 *@author LingLambda
 *@since 2025/3/13 17:48
 */
class SqlChatMemory : ChatMemory {
    override fun add(conversationId: String?, messages: MutableList<Message>?) {
        TODO("Not yet implemented")
    }

    override fun get(conversationId: String?, lastN: Int): MutableList<Message> {
        TODO("Not yet implemented")
    }

    override fun clear(conversationId: String?) {
        TODO("Not yet implemented")
    }
}