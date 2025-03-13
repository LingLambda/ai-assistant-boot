package com.ling.common.entity

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

/**
 *@author LingLambda
 *@since 2025/3/13 19:02
 */
@TableName("message_type")
class MessageType {
    @TableId
    var id: Int? = null
    var messageType: String? = null
}