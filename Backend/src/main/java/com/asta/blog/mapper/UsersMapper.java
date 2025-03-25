package com.asta.blog.mapper;

import com.asta.blog.models.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.security.core.userdetails.User;
import java.util.Optional;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Asta
 * @since 2025-03-24
 */
public interface UsersMapper extends BaseMapper<Users> {
    Optional<User> findByUsername(String username);
}
