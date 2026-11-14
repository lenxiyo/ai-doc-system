package com.lvent.doc.service.impl;

import com.lvent.common.utils.UserContext;
import com.lvent.doc.mapper.DocMapper;
import com.lvent.doc.model.entity.UploadFileMessage;
import com.lvent.doc.model.po.DocPO;
import com.lvent.doc.rabbitEnum.queue.RabbitQueue;
import com.lvent.doc.service.DocService;
import com.lvent.doc.utils.PdfUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocServiceImpl implements DocService {

    private final RabbitTemplate rabbitTemplate;

    private final DocMapper docMapper;

    @Override
    public Integer upload(MultipartFile multipartFile) throws IOException{
        if (multipartFile.isEmpty()){
            return -1;
        }
        // 文件上传并存储在  本地
        // 创建一个文件对象指向上传的文件所在位置和文件名
        String FILE_URL = "E:\\Software\\Projects\\ai-doc-system\\zupload\\";
        File file = new File(FILE_URL + multipartFile.getOriginalFilename());
        // 将上传的文件对象正式写入到file对象中
        multipartFile.transferTo(file);
        // byte[] fileBytes = multipartFile.getBytes();
        // 如果这个名字没有在Web端声明，那么会导致消息传递后被直接静默丢
        // 可以在RabbitMQ配置类中声明一个方法并返回我们需要的queue
        // String queueName = "doc.queue";
        // multipartFile对象本身不可序列化，而 RabbitTemplate.convertAndSend() 默认使用基于 JDK 序列化的 SimpleMessageConverter。不能直接传递multipartFile对象
        // 文件过小时（128KB）：我们可以构建一个DTO对象，这个DTO对象是可序列化的，然后将multipartFile对象的属性填入DTO对象属性中，然后传递这个DTO
        // byte[] 会占用消息体大小。RabbitMQ 默认 frame_max 为 128KB 左右，大文件可能超限。如果文件较大，不建议直接塞进消息体。
        // 文件过大时：选择使用外部存储，发消息时只传递文件路径
        // rabbitTemplate.convertAndSend(queueName, multipartFile);
        // MQ是异步开启一个线程去执行消费者的逻辑，两者的ThreadLocal并不互通，导致消费者拿不到用户信息
        UploadFileMessage uploadFileMessage = new UploadFileMessage(UserContext.getUser(),file.getPath());
        // MQ所使用的默认序列化器不能序列化对象，转为使用Jackson2JsonMessageConverter
        rabbitTemplate.convertAndSend(RabbitQueue.DOC_QUEUE.getQueueName(),uploadFileMessage);
        return 1;
    }

    @RabbitListener(queues = "doc.queue")
    public Integer extractText(UploadFileMessage uploadFileMessage) throws IOException{
        log.info("接收到{}中的消息:{}",RabbitQueue.DOC_QUEUE.getQueueName(),uploadFileMessage.toString());
        if (uploadFileMessage.getFilePath() == null){
            throw new IOException();
        }
        File file = new File(uploadFileMessage.getFilePath());
        String content = PdfUtil.extractText(file);
        log.info("文本解析完毕，准备存入数据库");
        DocPO docPO = new DocPO();
        docPO.setUserId(UserContext.getUser());
        docPO.setCreateTime(LocalDateTime.now());
        docPO.setFileName(file.getName());
        docPO.setContent(content);
        Integer rows = docMapper.save(docPO);
        if (rows == 0){
            // TODO 入库失败，觉得应该重试，等后期有实践再进行
            log.error("入库失败");
            return 0;
        }
        log.info("入库成功");
        return 1;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("topic.queue1"),
            exchange = @Exchange(value = "topic.exchange",type = ExchangeTypes.TOPIC),
            key = {"china.wheater","china.news"}
    ))
    public void test1Topic(String msg){
        log.info("test1Topic,{}",msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("topic.queue2"),
            exchange = @Exchange(value = "topic.exchange",type = ExchangeTypes.TOPIC),
            key = "china.*"
    ))
    public void test2Topic(String msg){
        log.info("test2Topic,{}",msg);
    }
}
