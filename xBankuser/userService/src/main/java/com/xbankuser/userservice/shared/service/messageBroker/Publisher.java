package com.xbankuser.userservice.shared.service.messageBroker;

import com.rabbitmq.client.Channel;
import com.xbankuser.userservice.config.Rabbitmq;
import org.springframework.stereotype.Service;

@Service
public class Publisher {
    private final static String QUEUE_NAME = "DEFAULT_QUEUE";
    private final static String EMAIL_QUEUE = "EMAIL_QUEUE";

    public void publishEmail(String message) throws Exception {
        try(Channel channel = Rabbitmq.createChannel()) {

//            channel.queueDeclare(EMAIL_QUEUE , false, false, false, null); // THis has already been done in a bean in the receiver class.
            channel.basicQos(1);
            channel.basicPublish("", EMAIL_QUEUE, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
