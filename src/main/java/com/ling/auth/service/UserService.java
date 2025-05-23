package com.ling.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ling.common.entity.User;
import com.ling.common.exception.InvalidCredentialsException;
import com.ling.common.exception.UsernameAlreadyExistsException;

/**
 * @author LingLambda
 * @since 2025/1/514:10
 */
public interface UserService extends IService<User> {
  /**
   * 根据用户名查询用户信息和身份名
   *
   * @param username 用户名
   */
  User queryUserAndRoleName(String username);

  /**
   * 登录
   */
  String login(User user) throws InvalidCredentialsException;

  /**
   * 注册
   */
  Boolean signUp(User user) throws UsernameAlreadyExistsException;
}
