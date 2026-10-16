package com.lvent.user.service;

import com.lvent.user.model.dto.UserDTO;
import com.lvent.user.model.vo.UserVO;

public interface UserService {
    Integer register(UserDTO userDTO);

    String login(String username, String password);
}
