package com.lvent.user.converter;

import com.lvent.user.model.dto.UserDTO;
import com.lvent.user.model.entity.User;
import com.lvent.user.model.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserConverter {
    // 在spring环境下不需要使用INSTANCE
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    // DTO转PO
    User UserDTOtoUserPO(UserDTO userDTO);

    // PO转VO
    UserVO UserPOtoUserVO(User user);
}
