package com.ling.chat.service

import com.baomidou.mybatisplus.extension.service.IService
import com.ling.common.entity.Room

/**
 *@author LingLambda
 *@since 2025/3/14 15:56
 */
interface RoomService : IService<Room> {
    fun createRoom(id: String?, userId: Long?)

    /**
     * 检查id对应的room是否存在，不存在则创建，有则更新update_time字段
     */
    fun checkRoomExist(conversationId: String?, userId: Long?)
    fun removeRoom(conversationId: String?)
}