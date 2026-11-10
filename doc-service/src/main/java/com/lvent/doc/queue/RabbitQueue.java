package com.lvent.doc.queue;


public enum RabbitQueue {
    /**
     * 默认使用的队列（测试队列）
     */
    SIMPLE_QUEUE("simple.RabbitMQQueue"),

    /**
     * doc模块使用的队列
     */
    DOC_QUEUE("doc.RabbitMQQueue");

    private final String queueName;

    RabbitQueue(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }
}
