package org.java.multiplex.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ApiReportReceive {

    @RabbitHandler
    @RabbitListener(queues = "api.report.payment")
    public void payment(String msg) {
        System.err.println("api.report.payment receive message: " + msg);
    }

    @RabbitHandler
    @RabbitListener(queues = "api.report.refund")
    public void refund(String msg) {
        System.err.println("api.report.refund receive message: " + msg);
    }
}
