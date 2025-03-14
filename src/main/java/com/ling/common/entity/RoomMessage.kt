package com.ling.common.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

/**
 *@author LingLambda
 *@since 2025/3/13 18:28
 */
@TableName("room_message")
class RoomMessage {
    @TableId
    var id: Long? = null
    var roomId: String? = null
    var message: String? = null
    var messageTypeId: Int? = null

    @TableField(exist = false)
    var room: Room? = null

    @TableField(exist = false)
    var messageType: MessageType? = null
}