package org.java.multiplex.rabbitmq.producer;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order implements Serializable {


    Integer id;
    String orderId;
    BigDecimal amount;
    Date createTime;


}
