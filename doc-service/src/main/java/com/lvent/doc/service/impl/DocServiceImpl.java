package com.lvent.doc.service.impl;

import com.lvent.doc.service.DocService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocServiceImpl implements DocService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public Integer upload(MultipartFile multipartFile) throws IOException{
        String originalFilename = multipartFile.getOriginalFilename();
        byte[] fileBytes = multipartFile.getBytes();
        // 如果这个名字没有在Web端声明，那么会导致消息传递后被直接静默丢
        // 可以在配置类中声明一个方法并返回我们需要的queue
        String queueName = "simple.queue";
        rabbitTemplate.convertAndSend(queueName, fileBytes);
        return 1;
    }


}
