package com.ling.chat.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.ling.chat.mapper.RoomMessageMapper
import com.ling.chat.service.RoomMessageService
import com.ling.common.entity.RoomMessage
import org.springframework.ai.chat.messages.Message
import org.springframework.stereotype.Service

@Service
open class RoomMessageServiceImpl(
    private val roomMessageMapper: RoomMessageMapper
) : ServiceImpl<RoomMessageMapper, RoomMessage>(), RoomMessageService {
    override fun addMessage(conversationId: String?, messages: MutableList<Message>?) {
        val roomMessage = RoomMessage()
        roomMessage.roomId = conversationId
        messages?.map { message ->
            roomMessage.message = message.text
            roomMessageMapper.insert(roomMessage)
        }
    }
}