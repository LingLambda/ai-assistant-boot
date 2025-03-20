package com.ling.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ling.auth.mapper.RoleMapper;
import com.ling.auth.service.RoleService;
import com.ling.common.entity.Role;
import org.springframework.stereotype.Service;

/**
 * @author LingLambda
 * @since 2025/1/514:10
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements
    RoleService {
}
