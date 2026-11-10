package com.lvent.doc.service.impl;

import com.lvent.doc.service.DocService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocServiceImpl implements DocService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public Integer upload(MultipartFile multipartFile) throws IOException{
        if (multipartFile.isEmpty()){
            return -1;
        }
        // 文件上传并存储在  本地
        // 创建一个文件对象指向上传的文件所在位置和文件名

        File file = new File("E:\\Software\\Projects\\ai-doc-system\\zupload\\" + multipartFile.getOriginalFilename());
        // 将上传的文件对象正式写入到file对象中
        multipartFile.transferTo(file);
        // 拿到文件路径，传递到生产者用于发消息
        String path = file.getPath();
        System.out.println(path);
        // byte[] fileBytes = multipartFile.getBytes();
        // 如果这个名字没有在Web端声明，那么会导致消息传递后被直接静默丢
        // 可以在RabbitMQ配置类中声明一个方法并返回我们需要的queue
        String queueName = "doc.queue";
        // multipartFile对象本身不可序列化，而而 RabbitTemplate.convertAndSend() 默认使用基于 JDK 序列化的 SimpleMessageConverter。
        // 文件过小时（128KB）：我们可以构建一个DTO对象，这个DTO对象是可序列化的，然后将multipartFile对象的属性填入DTO对象属性中，然后传递这个DTO
        //              byte[] 会占用消息体大小。RabbitMQ 默认 frame_max 为 128KB 左右，大文件可能超限。如果文件较大，不建议直接塞进消息体。
        // 文件过大时：选择使用外部存储，发消息时只传递文件路径
        // rabbitTemplate.convertAndSend(queueName, multipartFile);
        rabbitTemplate.convertAndSend(queueName,path);
        return 1;
    }

    @RabbitListener(queues = "doc.queue")
    public void extractText(MultipartFile file) throws IOException{
        String originalFilename = file.getOriginalFilename();
        String string = Arrays.toString(file.getBytes());
        System.out.println(string);
    }
}
