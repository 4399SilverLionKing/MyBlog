package com.asta.blog.models.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author Asta
 * @since 2025-03-24
 */
@Getter
@Setter
@ToString
@TableName("users")
public class Users implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId("id")
    private Integer id;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 用户密码
     */
    @TableField("user_password")
    private String userPassword;
}
