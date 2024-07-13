package com.xbankuser.userservice.shared.service.messageBroker;

import com.rabbitmq.client.Channel;
import com.xbankuser.userservice.config.Rabbitmq;

public class Receiver {
    private final static String QUEUE_NAME = "DEFAULT_QUEUE";

    public static void execute(String queueName) throws Exception {
        try(Channel channel = Rabbitmq.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Waiting for messages...........................");
            boolean autoAck = false;
            channel.basicConsume(QUEUE_NAME, autoAck, Rabbitmq.deliverCallback(channel), consumerTag -> { });
        }
    }
}