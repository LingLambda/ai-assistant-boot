package com.ling.common.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

/**
 *@author LingLambda
 *@since 2025/3/13 18:22
 */
@TableName("room")
class Room {
    @TableId(type = IdType.INPUT)
    var id: String? = null
    var createTime: LocalDateTime? = null
    var updateTime: LocalDateTime? = null
    var userId: Long? = null
    var title: String? = null

    @TableField(exist = false)
    var roomMessageList: List<RoomMessage>? = null
}