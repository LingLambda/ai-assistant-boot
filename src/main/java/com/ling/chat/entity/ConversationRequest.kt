package com.ling.chat.entity

/**
 * @author LingLambda
 * @since 2025/1/21 00:03
 */
data class ConversationRequest(
    var conversationId: String? = null,
    var message: String? = null
)