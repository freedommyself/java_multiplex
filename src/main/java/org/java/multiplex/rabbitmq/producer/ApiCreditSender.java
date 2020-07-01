package org.java.multiplex.rabbitmq.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ApiCreditSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void creditBank(Map<String, Object> head, String msg) {
        System.err.println("credit.bank send message: " + msg);
        rabbitTemplate.convertAndSend("creditBankExchange", "credit.bank", getMessage(head, msg));
    }

    private Obj getMessage(Map<String, Object> head, String msg) {
        /*Obj obj = new Obj();
        obj.setHead(head);
        obj.setMsg(msg);
        return obj;*/
        return null;
    }

    public void creditFinance(Map<String, Object> head, String msg) {
        System.err.println("credit.finance send message: " + msg);
        rabbitTemplate.convertAndSend("creditFinanceExchange", "credit.finance", getMessage(head, msg));
    }


}
