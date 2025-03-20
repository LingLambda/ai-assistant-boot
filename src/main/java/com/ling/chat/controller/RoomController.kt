package com.ling.chat.controller

import com.ling.auth.util.JwtUtil
import com.ling.chat.service.RoomMessageService
import com.ling.chat.service.RoomService
import com.ling.common.entity.Room
import com.ling.common.util.Result
import org.springframework.ai.chat.messages.Message
import org.springframework.web.bind.annotation.*

/**
 *@author LingLambda
 *@since 2025/3/14 15:47
 */
@RestController
@RequestMapping("room")
@CrossOrigin
class RoomController(
    private val roomService: RoomService,
    private val roomMessageService: RoomMessageService
) {
    /**
     * 根据用户id查询所有房间，从jwt获取userId防止伪造
     */
    @GetMapping("query_room")
    fun queryRoomByUser(@RequestHeader("Authorization") token: String): Result<MutableList<Room>> {
        val userId: Long = JwtUtil.getUserIdFromToken(token)
        val rooms = roomService.queryRoomByUserId(userId)
        return Result.ok(rooms)
    }

    @GetMapping("query_room_message")
    fun queryRoomMessage(conversationId: String): Result<MutableList<Message>> {
        val messages = roomMessageService.getMessage(conversationId, 100)
        return Result.ok(messages)
    }
}