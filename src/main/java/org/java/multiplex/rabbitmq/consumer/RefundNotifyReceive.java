package org.java.multiplex.rabbitmq.consumer;

import org.java.multiplex.rabbitmq.producer.Order;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "notify.refund")
public class RefundNotifyReceive {

    @RabbitHandler
    public void receive(Order order) {
        System.err.println("notify.refund receive message: " + order);
    }
}
