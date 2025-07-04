package com.asta.blog.service.impl;

import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.asta.blog.mapper.UsersMapper;

import java.util.Set;
import java.util.stream.Collectors;

/**
 *  MyUserDetailsService 类实现了 UserDetailsService 接口，
 *  用于从数据库中加载用户详细信息，以便 Spring Security 进行身份验证和授权。
 */
@Service 
public class MyUserDetailsService implements UserDetailsService {

    @Resource
    private UsersMapper userMapper;

    /**
     *  根据用户名加载用户详细信息。
     *  此方法是 UserDetailsService 接口的实现，Spring Security 在身份验证过程中会调用此方法。
     *
     * @param username 用户名
     * @return UserDetails 用户详细信息，包含用户名、密码和权限等
     * @throws UsernameNotFoundException 如果根据用户名找不到用户，则抛出此异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // 调用 userMapper 的 findByUsername 方法根据用户名从数据库中查找用户
            User user = userMapper.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username or Email"));

            
            // 从 User 实体中获取用户的权限信息，并将其转换为 Spring Security 的 GrantedAuthority 集合
            Set<GrantedAuthority> authorities = user.getAuthorities().stream()
                    .map((role) -> new SimpleGrantedAuthority(role.getAuthority()))
                    .collect(Collectors.toSet());

            
            // 创建 Spring Security 的 UserDetails 对象并返回
            // UserDetails 是 Spring Security 定义的用户信息接口，包含了进行身份验证和授权所需的用户信息
            return new org.springframework.security.core.userdetails.User(
                    username,              // 用户名
                    user.getPassword(),      // 密码
                    authorities             // 权限集合
            );
        } catch (Exception e) {
            throw e;
        }
    }
}
