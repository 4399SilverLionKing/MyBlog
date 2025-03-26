package com.asta.blog.mapper;

import com.asta.blog.models.entity.Users;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Asta
 * @since 2025-03-24
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {
    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return 包含Spring Security User对象的Optional
     */
    default Optional<User> findByUsername(String username) {
        // 构建查询条件
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", username);
        
        // 执行查询
        Users user = selectOne(wrapper);
        
        // 如果用户不存在，返回空Optional
        if (user == null) {
            return Optional.empty();
        }
        
        // 创建权限集合 - 这里为简单起见，给所有用户添加一个USER角色
        Set<GrantedAuthority> authorities = Collections.singleton(
            new SimpleGrantedAuthority("ROLE_USER")
        );
        
        // 创建并返回Spring Security User对象
        return Optional.of(
            new User(
                user.getUserName(),
                user.getUserPassword(),
                authorities
            )
        );
    }
}
