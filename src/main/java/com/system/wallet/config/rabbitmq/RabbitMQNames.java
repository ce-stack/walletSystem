package com.system.wallet.config.rabbitmq;
public final class RabbitMQNames {

    public static final String WALLET_EVENTS_EXCHANGE = "wallet.events.exchange";
    public static final String TRANSFER_COMPLETED_QUEUE = "transfer.completed.queue";
    public static final String TRANSFER_COMPLETED_ROUTING_KEY = "transfer.completed";

    private RabbitMQNames() {
    }
}
