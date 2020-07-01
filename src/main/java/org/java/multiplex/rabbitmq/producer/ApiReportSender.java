package org.java.multiplex.rabbitmq.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiReportSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void generateReports(String msg) {
        System.err.println("api.generate.reports send message: " + msg);
        rabbitTemplate.convertAndSend("reportExchange", "api.generate.reports", msg);
    }

}
