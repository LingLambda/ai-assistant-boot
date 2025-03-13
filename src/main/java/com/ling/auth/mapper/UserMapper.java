package com.ling.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ling.common.entity.User;

/**
 * @author LingLambda
 * @date 2025/1/514:07
 */
public interface UserMapper extends BaseMapper<User> {

  /**
   * 根据username查user与user的role
   *
   * @param username 用户名
   */
  User queryUserAndRole(String username);
}
