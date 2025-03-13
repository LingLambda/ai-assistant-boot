package com.ling.chat.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.ling.common.entity.MessageType
import org.apache.ibatis.annotations.Mapper

/**
 *@author LingLambda
 *@since 2025/3/13 19:24
 */
@Mapper
interface MessageTypeMapper : BaseMapper<MessageType>