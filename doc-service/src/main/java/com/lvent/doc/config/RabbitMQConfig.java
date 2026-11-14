package com.lvent.doc.config;

import com.lvent.doc.rabbitEnum.exchange.RabbitExchange;
import com.lvent.doc.rabbitEnum.queue.RabbitQueue;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQConfig {
    private final RabbitTemplate rabbitTemplate;

    // 注入（后续可能导致循环依赖问题，需要将@Bean单独拿出来作为另一份配置文件切断循环依赖问题）
    public RabbitMQConfig(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 消息转化器，默认jdk序列化，改为json转化器
     */
    // rabbitTemplate在创建时是使用延迟注入机制的
    // 在实例化Template时，并不会死死等待MessageConverter准备好，而是先占住该位置，等到实例化完成后再注册messageConverter，然后拿给rabbitTemplate
    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    /**
     * Simple Queue
     */
    @Bean
    public Queue simpleQueue() {
        // true 代表持久化
        return new Queue(RabbitQueue.SIMPLE_QUEUE.getQueueName(), true);
    }

    /**
     * Doc Queue
     * 用于文件上传时使用的MQ队列
     */
    @Bean
    public Queue docQueue() {
        return new Queue(RabbitQueue.DOC_QUEUE.getQueueName(), true);
    }

    /**
     * 防止交换机-->队列处导致消息丢失
     */
    // @PostConstruct 是在依赖注入完成之后执行的，所以可以直接使用已经注入的 rabbitTemplate 对象来设置回调。不需要通过Spring从容器中拿到需要的bean
    @PostConstruct
    public void initRabbitCallbacks() {
        rabbitTemplate.setReturnsCallback(returned ->
                log.error("消息路由失败! 应答码={}, 原因={}, 交换机={}, 路由键={}, 消息={}",
                        returned.getReplyCode(),
                        returned.getReplyText(),
                        returned.getExchange(),
                        returned.getRoutingKey(),
                        returned.getMessage()));
    }
}
