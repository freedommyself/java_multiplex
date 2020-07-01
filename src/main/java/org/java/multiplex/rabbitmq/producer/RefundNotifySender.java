package org.java.multiplex.rabbitmq.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RefundNotifySender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void sender(Order order) {
        System.err.println("notify.refund send message: " + order);
        rabbitTemplate.convertAndSend("notify.refund", order);
    }
}


