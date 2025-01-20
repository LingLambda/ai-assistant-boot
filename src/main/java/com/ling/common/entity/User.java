package com.ling.common.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User {
  @TableId
  private Long id;
  private String username;
  private String password;
  private Integer roleId;
  private String salt;
  @TableField(exist = false)
  private Role role;
}
