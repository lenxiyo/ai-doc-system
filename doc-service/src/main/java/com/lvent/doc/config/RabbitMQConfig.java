package com.lvent.doc.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue simpleQueue() {
        // true 代表持久化
        return new Queue("simple.queue",true);
    }

    @Bean
    // 用于文件上传时使用的MQ队列
    public Queue docQueue(){
        return new Queue("doc.queue",true);
    }
}
