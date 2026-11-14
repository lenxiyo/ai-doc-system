package com.lvent.doc.rabbitEnum.exchange;

import lombok.Getter;

@Getter
public enum RabbitExchange {
    /**
     * 用于doc模块使用的exchange
     */
    DOC_FANOUT("doc.fanout");

    private final String exchangeName;

    RabbitExchange(String exchangeName) {
        this.exchangeName = exchangeName;
    }

}
