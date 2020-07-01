package org.java.multiplex.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "notify.payment")
public class PaymentNotifyReceive {

    @RabbitHandler
    public void receive(String msg) {
        System.err.println("notify.payment receive message: " + msg);
    }
}
