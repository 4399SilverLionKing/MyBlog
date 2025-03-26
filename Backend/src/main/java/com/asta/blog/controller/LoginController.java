package com.asta.blog.controller;

import com.asta.blog.models.dto.LoginDTO;
import com.asta.blog.models.vo.ResultStatus;
import com.asta.blog.service.impl.MyUserDetailsService;
import com.asta.blog.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import com.asta.blog.models.vo.JsonVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;


@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private MyUserDetailsService userDetailsService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate/login")
    public JsonVO<Object> login(@RequestBody LoginDTO dto){
        try {
            // 尝试进行身份验证
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    dto.getUserName(), 
                    dto.getUserPassword()
                )
            );
        } catch (BadCredentialsException e) {
            // 认证失败，返回错误信息
            return JsonVO.create(null, ResultStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return JsonVO.create(null, ResultStatus.SERVER_ERROR);
        }
        // 认证成功，加载用户详情
        final UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUserName());

        // 生成JWT令牌
        final String jwt = jwtUtil.generateToken(userDetails);
        
        // 创建包含令牌的响应
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("username", userDetails.getUsername());
        
        // 返回成功响应，包含JWT令牌和用户信息
        return JsonVO.success(response);
    }
}
