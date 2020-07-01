package org.java.multiplex.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ApiCoreReceive {

    @RabbitHandler
    @RabbitListener(queues = "api.core")
    public void user(String msg) {
        System.err.println("api.core receive message: " + msg);
    }
}
