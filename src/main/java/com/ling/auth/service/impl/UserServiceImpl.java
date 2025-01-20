package com.ling.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ling.common.exception.InvalidCredentialsException;
import com.ling.common.exception.UsernameAlreadyExistsException;
import com.ling.auth.mapper.UserMapper;
import com.ling.auth.service.UserService;
import com.ling.auth.util.JwtUtil;
import com.ling.auth.util.SHA256SaltedUtil;
import com.ling.common.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
 * @author LingLambda
 * @date 2025/1/514:11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

  private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
  private final UserMapper userMapper;

  public UserServiceImpl(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public User queryUserAndRoleName(String username) {
    return null;
  }

  @Override
  public String login(User user) throws InvalidCredentialsException {
    try {
      User selectOne = userMapper.queryUserAndRole(user.getUsername());
      if (selectOne == null) {
        throw new InvalidCredentialsException("用户名或密码错误");
      }
      String saltPwd = SHA256SaltedUtil.sha256WithSalt(user.getPassword(), selectOne.getSalt());
      if (!saltPwd.equals(selectOne.getPassword())) {
        throw new InvalidCredentialsException("用户名或密码错误");
      }

      return JwtUtil.generateJwt(selectOne.getUsername(), selectOne.getRoleId(), selectOne.getRole().getRoleName());
    } catch (NoSuchAlgorithmException e) {
      log.error("无此加密方式{}", e.getMessage());
    }
    return null;
  }

  @Override
  public Boolean signUp(User user) throws UsernameAlreadyExistsException {
    try {
      LambdaQueryWrapper<User> selectWrapper = new LambdaQueryWrapper<>();
      LambdaQueryWrapper<User> select = selectWrapper.eq(User::getUsername, user.getUsername());
      if (userMapper.exists(select)) {
        throw new UsernameAlreadyExistsException("用户名已存在");
      }
      String salt = SHA256SaltedUtil.generateSalt(16);
      String saltPwd = null;
      saltPwd = SHA256SaltedUtil.sha256WithSalt(user.getPassword(), salt);
      if (userMapper.insert(new User(null,user.getUsername(),saltPwd,1,salt,null))!=0) {
        return true;
      }
    } catch (NoSuchAlgorithmException e) {
      log.error("无此加密方式{}", e.getMessage());
    }
    return false;
  }
}
