package com.ling.common.entity

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
/**
 *@author LingLambda
 *@since 2025/3/7 14:11
 */
@TableName("role")
class Role {
    @TableId
    var id: Int? = null
    var roleName: String? = null
}