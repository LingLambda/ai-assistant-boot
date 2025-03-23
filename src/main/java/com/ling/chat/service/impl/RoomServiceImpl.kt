package com.ling.chat.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.ling.chat.mapper.RoomMapper
import com.ling.chat.mapper.RoomMessageMapper
import com.ling.chat.service.RoomService
import com.ling.common.entity.Room
import com.ling.common.entity.RoomMessage
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
open class RoomServiceImpl(
    private val roomMapper: RoomMapper,
    private val roomMessageMapper: RoomMessageMapper
) : ServiceImpl<RoomMapper, Room>(), RoomService {
    override fun createRoom(id: String?, userId: Long?) {
        val room = Room()
        room.id = id
        room.userId = userId
        room.createTime = LocalDateTime.now()
        room.updateTime = LocalDateTime.now()
        roomMapper.insert(room)
    }

    override fun checkRoomCreateOrUpdate(
        conversationId: String?,
        userId: Long?
    ) {
        val selectById = roomMapper.selectById(conversationId)
        val room = Room()
        room.id = conversationId
        room.updateTime = LocalDateTime.now()
        if (selectById == null) {
            room.createTime = LocalDateTime.now()
            room.userId = userId
        }
        roomMapper.insertOrUpdate(room)
    }

    override fun removeRoom(conversationId: String?) {
        roomMapper.deleteById(conversationId)
        val updateWrapper =
            UpdateWrapper<RoomMessage>().eq("room_id", conversationId)
        roomMessageMapper.delete(updateWrapper)
    }

    override fun queryRoomByUserId(userId: Long?): MutableList<Room> {
        val wrapper =
            QueryWrapper<Room>().eq("user_id", userId)
                .orderByDesc("create_time")
        return roomMapper.selectList(wrapper)
    }

    override fun setTitle(conversationId: String, title: String) {
        val wrapper = UpdateWrapper<Room>().eq("id", conversationId)
            .set("title", title)
        roomMapper.update(wrapper)
    }
}