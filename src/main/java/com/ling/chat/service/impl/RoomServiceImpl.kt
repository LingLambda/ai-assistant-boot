package com.ling.chat.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.ling.chat.mapper.RoomMapper
import com.ling.chat.service.RoomService
import com.ling.common.entity.Room
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
open class RoomServiceImpl(private val roomMapper: RoomMapper) : ServiceImpl<RoomMapper,Room>(),RoomService {
    override fun createRoom(id:String?,userId: Long?) {
        val room = Room()
        room.id = id
        room.userId=userId
        room.createTime = LocalDateTime.now()
        room.updateTime = LocalDateTime.now()
        roomMapper.insert(room)
    }
}