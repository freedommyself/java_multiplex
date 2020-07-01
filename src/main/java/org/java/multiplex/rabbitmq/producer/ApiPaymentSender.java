package org.java.multiplex.rabbitmq.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiPaymentSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void order(String msg) {
        System.err.println("api.payment.order send message: " + msg);
        rabbitTemplate.convertAndSend("paymentExchange", "api.payment.order", msg);
    }

    public void orderQuery(String msg) {
        System.err.println("api.payment.order.query send message: " + msg);
        rabbitTemplate.convertAndSend("paymentExchange", "api.payment.order.query", msg);
    }

    public void orderDetailQuery(String msg) {
        System.err.println("api.payment.order.detail.query send message: " + msg);
        rabbitTemplate.convertAndSend("paymentExchange", "api.payment.order.detail.query", msg);
    }
}
