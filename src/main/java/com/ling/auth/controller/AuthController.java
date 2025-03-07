package com.ling.auth.controller;

import com.ling.auth.service.UserService;
import com.ling.common.entity.User;
import com.ling.common.exception.InvalidCredentialsException;
import com.ling.common.exception.UsernameAlreadyExistsException;
import com.ling.common.util.Result;
import com.ling.common.util.ResultCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * @author LingLambda
 * @date 2025/1/517:29
 */
@RestController
@RequestMapping("auth")
@CrossOrigin
public class AuthController {

  private static final Logger log = LoggerFactory.getLogger(AuthController.class);
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("sign_up")
  public Result<?> signUp(@RequestBody User user) {
    try {
      if (userService.signUp(user)) {
        return Result.ok();
      }
      return Result.fail();
    } catch (UsernameAlreadyExistsException e) {
      return Result.build(e.getMessage(), ResultCodeEnum.FAIL);
    } catch (Exception e) {
      log.error("未知错误", e);
      return Result.build("未知内部错误", ResultCodeEnum.FAIL);
    }
  }

  @PostMapping("login")
  public Result<String> login(@RequestBody User user) {
    try {
      String token = userService.login(user);
      return Result.ok(token);
    } catch (InvalidCredentialsException e) {
      return Result.build(e.getMessage(), ResultCodeEnum.LOGIN_ERROR);
    } catch (Exception e) {
      log.error("未知错误", e);
      return Result.build("未知内部错误", ResultCodeEnum.FAIL);
    }
  }
}
