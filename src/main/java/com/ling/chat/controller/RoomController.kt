package com.ling.chat.controller

import com.ling.chat.mapper.RoomMapper
import com.ling.chat.service.RoomService
import org.springframework.ai.chat.client.ChatClient
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *@author LingLambda
 *@since 2025/3/14 15:47
 */
@RestController
@RequestMapping("room")
@CrossOrigin
class RoomController(
    private var chatClient: ChatClient?,
    private val roomMapper: RoomMapper,
    private val roomService: RoomService
) {
}