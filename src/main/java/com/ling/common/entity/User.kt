package com.ling.common.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName("user")
class User {
    @TableId
    var id: Long? = null
    var username: String? = null
    var password: String? = null
    var roleId: Int? = null
    var salt: String? = null

    @TableField(exist = false)
    var role: Role? = null
}
