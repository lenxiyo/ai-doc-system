package com.lvent.user.service.impl;

import com.lvent.user.converter.UserConverter;
import com.lvent.user.model.dto.UserDTO;
import com.lvent.user.model.entity.User;
import com.lvent.user.mapper.UserMapper;
import com.lvent.user.model.vo.UserVO;
import com.lvent.user.service.UserService;
import com.lvent.user.utils.JwtUtil;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public Integer register(UserDTO userDTO) {
        User user = UserConverter.INSTANCE.UserDTOtoUserPO(userDTO);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        // TODO 注册时密码未加密，后期增加
        return userMapper.register(user);
    }

    @Override
    public String login(String username, String password) {
        User user = userMapper.getUser(username,password);
        if (user != null){
            String jwt = JwtUtil.GenJWT(user.getId(), user.getUsername());
            log.info("用户{}此次登录token为{}",user.getId(),jwt);
            return jwt;
        }else {
            log.info("未找到该用户:{}",username);
            return null;
        }
    }
}
