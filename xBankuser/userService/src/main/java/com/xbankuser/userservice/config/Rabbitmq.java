package com.xbankuser.userservice.config;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xbankuser.userservice.shared.service.messageBroker.Receiver;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Configuration
public class Rabbitmq {

    public static final String QUEUE_NAME = "myQueue";
    public static final String EXCHANGE_NAME = "myExchange";
    public static final String ROUTING_KEY = "myRoutingKey";

    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public static Channel createChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        System.out.println("*******************---RABBITMQ CONNECTION CREATED---**************************");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            return channel;
        }
    }

    public static DeliverCallback deliverCallback(Channel channel) {
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

            System.out.println(" [x] Received '" + message + "'");
            try {
                //doWork(message);
            } finally {
                System.out.println(" [x] Done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
    }
}