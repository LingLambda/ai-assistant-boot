package com.ling.chat.service

import com.baomidou.mybatisplus.extension.service.IService
import com.ling.common.entity.RoomMessage
import org.springframework.ai.chat.messages.Message

/**
 *@author LingLambda
 *@since 2025/3/14 16:56
 */
interface RoomMessageService : IService<RoomMessage> {
    fun addMessage(conversationId: String?, messages: MutableList<Message>?)
    fun getMessage(conversationId: String?, lastN: Int?): MutableList<Message>
    fun clearMessage(conversationId: String?)
}