package com.ling.chat.service.impl

import com.ling.chat.service.ConversationSummaryService
import com.ling.chat.service.RoomMessageService
import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Service

@Service
class OpenaiConversationSummary(
    private val roomMessageService: RoomMessageService,
    private val lingClient: ChatClient
) :
    ConversationSummaryService {
    override fun summaryTitle(conversationId: String): String {
        val messagesStr =
            roomMessageService.getMessage(conversationId, 2).toString()
        return lingClient
            .prompt("According to the user's language. Summarizing a conversation into a concise title. Do not use quotation marks. Below are the conversation messages: $messagesStr")
            .call()
            .content() ?: "新对话"
    }
}