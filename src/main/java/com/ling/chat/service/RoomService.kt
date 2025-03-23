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
    fun checkRoomCreateOrUpdate(conversationId: String?, userId: Long?)

    /**
     * 删除房间并清空其下所有消息记录
     */
    fun removeRoom(conversationId: String?)

    /**
     * 查询某用户的所有消息房间 根据时间倒序
     */
    fun queryRoomByUserId(userId: Long?): MutableList<Room>

    /**
     * 设置标题
     */
    fun setTitle(conversationId: String, title: String)
}