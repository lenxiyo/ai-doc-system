package com.lvent.doc.rabbitEnum.queue;

import lombok.Getter;

@Getter
public enum RabbitQueue {
    /**
     * 默认使用的队列（测试队列）
     */
    SIMPLE_QUEUE("simple.queue"),

    /**
     * doc模块使用的队列
     */
    DOC_QUEUE("doc.queue");

    private final String queueName;

    RabbitQueue(String queueName) {
        this.queueName = queueName;
    }
}
