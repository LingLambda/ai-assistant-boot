package com.ling.chat.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.ling.chat.mapper.RoomMessageMapper
import com.ling.chat.service.RoomMessageService
import com.ling.common.entity.RoomMessage
import org.springframework.ai.chat.messages.*
import org.springframework.stereotype.Service

@Service
open class RoomMessageServiceImpl(
  private val roomMessageMapper: RoomMessageMapper
) : ServiceImpl<RoomMessageMapper, RoomMessage>(), RoomMessageService {
  override fun addMessage(
    conversationId: String?,
    messages: MutableList<Message>?
  ) {
    val roomMessage = RoomMessage()
    roomMessage.roomId = conversationId
    messages?.map { message ->
      roomMessage.message = message.text
      roomMessage.messageType = when (message.messageType) {
        MessageType.SYSTEM -> MessageType.SYSTEM.value
        //MessageType.TOOL -> MessageType.TOOL.value
        MessageType.ASSISTANT -> MessageType.ASSISTANT.value
        MessageType.USER -> MessageType.USER.value
        else -> throw RuntimeException("Non-existent message type")
      }
      roomMessageMapper.insert(roomMessage)
    }
  }

  override fun getMessage(
    conversationId: String?,
    lastN: Int?
  ): MutableList<Message> {
    val queryWrapper = QueryWrapper<RoomMessage>()
    queryWrapper.eq("room_id", conversationId)
    val selectList = roomMessageMapper.selectList(queryWrapper)
    val messageList: MutableList<Message> = selectList.map { roomMessage ->
      when (roomMessage.messageType) {
        MessageType.SYSTEM.value -> SystemMessage(roomMessage.message)
        //MessageType.TOOL.value -> ToolResponseMessage(null)
        MessageType.ASSISTANT.value -> AssistantMessage(roomMessage.message)
        MessageType.USER.value -> UserMessage(roomMessage.message)
        else -> throw RuntimeException("Non-existent message type")
      }
    }.toMutableList()
    val size = messageList.size
    val fromIndex = maxOf(0, size - (lastN ?: error("lastN is null")))
    return messageList.subList(fromIndex, size)
  }

  override fun clearMessage(conversationId: String?) {
    val queryWrapper =
      QueryWrapper<RoomMessage>().eq("room_id", conversationId)
    roomMessageMapper.delete(queryWrapper)
  }
}