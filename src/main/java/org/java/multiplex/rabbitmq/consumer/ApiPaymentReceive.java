package org.java.multiplex.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ApiPaymentReceive {

    @RabbitHandler
    @RabbitListener(queues = "api.payment")
    public void order(String msg) {
        System.err.println("api.payment.order receive message: " + msg);
    }

}
