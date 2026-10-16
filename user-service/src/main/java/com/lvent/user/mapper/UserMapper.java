package com.lvent.user.mapper;

import com.lvent.user.model.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into " +
            "user (username,password,user_image,create_time,update_time) " +
            "values " +
            "(#{username},#{password},#{userImage},#{createTime},#{updateTime});")
    Integer register(User user);

    @Select("select * from user where username = #{username} and password = #{password}")
    User getUser(String username, String password);
}
