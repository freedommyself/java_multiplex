package org.java.multiplex.rabbitmq.producer;

import lombok.Data;

import java.util.Map;

@Data
public class Obj {
    Map head;
    String msg;
}
