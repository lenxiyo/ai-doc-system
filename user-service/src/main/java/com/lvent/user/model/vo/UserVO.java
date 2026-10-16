package com.lvent.user.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    // 在展示层添加为user+id的形式，id主键自增
    private String userId;
    private String username;
    private String userImage;
    private LocalDateTime createTime;
}
