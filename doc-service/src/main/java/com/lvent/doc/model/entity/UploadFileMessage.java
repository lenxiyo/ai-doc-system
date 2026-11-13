package com.lvent.doc.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 该类用于MQ异步发送消息时携带用户信息和文件路径
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileMessage {
    private Integer userInfo;
    private String filePath;
}
