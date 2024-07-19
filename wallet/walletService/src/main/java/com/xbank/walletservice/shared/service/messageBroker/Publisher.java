package com.xbank.walletservice.shared.service.messageBroker;

import com.rabbitmq.client.Channel;
import com.xbank.walletservice.config.Rabbitmq;

public class Publisher {
    private final static String QUEUE_NAME = "DEFAULT_QUEUE";

    public static void execute(String message, String queueName) throws Exception {
        try(Channel channel = Rabbitmq.createChannel()) {

            channel.queueDeclare(queueName.isBlank()?QUEUE_NAME: queueName , false, false, false, null);
            channel.basicQos(1);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
