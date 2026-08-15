package com.system.wallet.services;


import com.system.wallet.config.rabbitmq.RabbitMQNames;
import com.system.wallet.models.Outbox_event;
import com.system.wallet.repositories.OutboxEventsRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OutboxPublisherWorkerService {

    private final OutboxEventsRepository outboxEventsRepository;
    private final RabbitTemplate rabbitTemplate;

    public OutboxPublisherWorkerService(OutboxEventsRepository outboxEventsRepository, RabbitTemplate rabbitTemplate) {
        this.outboxEventsRepository = outboxEventsRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void publishPendingEvents(){
        List<Outbox_event> events = outboxEventsRepository.findTop10ByStatusOrderByCreatedAtAsc("PENDING");

        for (Outbox_event event : events) {
            try {
                rabbitTemplate.convertAndSend(
                        RabbitMQNames.WALLET_EVENTS_EXCHANGE,
                        RabbitMQNames.TRANSFER_COMPLETED_ROUTING_KEY,
                        event.getPayload()
                );

                event.setStatus("PUBLISHED");
                event.setPublished_at(new Date());
            }catch(Exception e){
                event.setRetry_count(event.getRetry_count() + 1);

                if (event.getRetry_count() >= 3) {
                    event.setStatus("FAILED");
                }
            }
        }
    }
}
