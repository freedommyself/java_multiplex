package org.java.multiplex.rabbitmq.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueryOrderSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void sender(String orderId) {
        System.err.println("query.order send message: " + orderId);
        Order order = (Order) rabbitTemplate.convertSendAndReceive("query.order", orderId);
        System.err.println("query.order return message: " + order);
    }

}
