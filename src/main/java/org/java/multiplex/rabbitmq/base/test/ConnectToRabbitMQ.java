package org.java.multiplex.rabbitmq.base.test;

import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ConnectToRabbitMQ {

    public static void main(String[] args) throws Exception {
        Logger log = LoggerFactory.getLogger(ConnectToRabbitMQ.class);

        ConnectionFactory factory = new ConnectionFactory();
        /*factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        factory.setHost("localhost");
        factory.setPort(5672);*/

        factory.setUri("amqp://guest:guest@localhost:5672");
        Connection conn = factory.newConnection();
        log.info(conn.toString());
        Channel channel = conn.createChannel();
        log.info(channel.toString());
        /*channel.close();
        conn.close();*/

        channel.exchangeDeclare("ex1", "direct", true);
            /*String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, "ex1", "default_queue_name");*/
        channel.queueDeclare("que1", true, false, false, null);
        channel.queueBind("que1", "ex1", "default_queue_name");

        DeclareOk response = channel.queueDeclarePassive("que1");
        log.error(response.getMessageCount() + "");
        log.error(response.getConsumerCount() + "");

        /*channel.queuePurge("que1");*/
    }

    public static void sendMessage(Channel channel, String exName) throws IOException {
        byte[] messageBodyBytes = "你好,世界".getBytes();
        /*channel.basicPublish(exName, "default_queue_name", null, messageBodyBytes);*/
        channel.basicPublish(exName, "default_queue_name", true,
                MessageProperties.PERSISTENT_TEXT_PLAIN, messageBodyBytes);
    }
}
