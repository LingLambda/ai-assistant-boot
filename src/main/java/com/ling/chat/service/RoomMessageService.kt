package com.ling.chat.service

import com.baomidou.mybatisplus.extension.service.IService
import com.ling.common.entity.RoomMessage
import org.springframework.ai.chat.messages.Message

/**
 *@author LingLambda
 *@since 2025/3/14 16:56
 */
interface RoomMessageService : IService<RoomMessage> {
    /**
     * 在指定房间添加消息
     */
    fun addMessage(conversationId: String?, messages: MutableList<Message>?)

    /**
     * 获取指定房间的所有消息
     */
    fun getMessage(conversationId: String?, lastN: Int?): MutableList<Message>

    /**
     * 清空指定房间消息
     */
    fun clearMessage(conversationId: String?)
}