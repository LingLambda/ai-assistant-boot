package com.ling.chat.controller

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.ling.auth.util.JwtUtil
import com.ling.chat.mapper.RoomMapper
import com.ling.chat.service.RoomService
import com.ling.common.entity.Room
import org.springframework.ai.chat.client.ChatClient
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
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
){
}