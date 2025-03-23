package com.ling.chat.service

/**
 *@author LingLambda
 *@since 2025/3/20 17:38
 */
interface ConversationSummaryService {
    /**
     * 房间的第一次对话，本函数会调用模型执行一次对话总结，使其成为房间标题
     */
    fun summaryTitle(conversationId: String): String
}