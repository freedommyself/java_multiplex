package org.java.multiplex.rabbitmq.consumer;

import org.java.multiplex.rabbitmq.producer.Order;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
@RabbitListener(queues = "query.order")
public class QueryOrderReceive {

    @RabbitHandler
    public Order receive(String orderId) {
        System.err.println("notify.refund receive message: " + orderId);

        /*Order order = new Order();
        order.setId(100001);
        order.setOrderId(orderId);
        order.setAmount(new BigDecimal("2999.99"));
        order.setCreateTime(new Date());*/
//        return order;
        return null;
    }
}
