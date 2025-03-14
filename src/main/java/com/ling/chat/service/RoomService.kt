package com.ling.chat.service

import com.baomidou.mybatisplus.extension.service.IService
import com.ling.common.entity.Room

/**
 *@author LingLambda
 *@since 2025/3/14 15:56
 */
interface RoomService:IService<Room> {
    fun createRoom(id:String?,userId: Long?)
}