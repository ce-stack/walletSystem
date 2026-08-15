package com.system.wallet.consumers;

import com.system.wallet.config.rabbitmq.RabbitMQNames;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TransferCompletedConsumer {

    @RabbitListener(queues = RabbitMQNames.TRANSFER_COMPLETED_QUEUE)
    public void consumeTransferCompletedEvent(String payload) {
        System.out.println("Transfer completed event received:");
        System.out.println(payload);
    }
}
