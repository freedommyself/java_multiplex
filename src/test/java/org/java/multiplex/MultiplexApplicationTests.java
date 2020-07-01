package org.java.multiplex;

import org.java.multiplex.rabbitmq.producer.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class MultiplexApplicationTests {

    @Autowired
    private PaymentNotifySender sender;
    @Autowired
    private ApiCoreSender sender_core;
    @Autowired
    private ApiPaymentSender sender_pay;
    @Autowired
    private ApiCreditSender sender_credit;
    @Autowired
    private ApiReportSender sender_report;
    @Autowired
    private RefundNotifySender sender_refund;
    @Autowired
    private QueryOrderSender sender_order;

    @Test
    void contextLoads() {
//        sender.sender("支付订单号：" + System.currentTimeMillis());
//        sender_api.user("用户管理！");
//        sender_api.userQuery("查询用户信息！");
//        sender_pay.order("订单管理！");
//        sender_pay.orderQuery("查询订单信息！");
//        sender_pay.orderDetailQuery("查询订单详情信息！");
        Map<String, Object> head = new HashMap<>();
        head.put("type", "cash");
        sender_credit.creditBank(head, "银行授信(部分匹配)");
//        sender_report.generateReports("开始生成报表！");
//        Order order = new Order();
//        order.setId(100001);
//        order.setOrderId(String.valueOf(System.currentTimeMillis()));
//        order.setAmount(new BigDecimal("1999.99"));
//        order.setCreateTime(new Date());
//        sender_refund.sender(order);
//        sender_order.sender("900000001");
    }

    @Test
    void contextLoads2() {

    }

}
