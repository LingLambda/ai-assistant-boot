package com.ling.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ling.common.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author LingLambda
 * @date 2025/1/514:07
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

  /**
   * 根据username查user与user的role
   *
   * @param username 用户名
   */
  User queryUserAndRole(String username);
}
