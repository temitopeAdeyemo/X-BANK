package com.xbankuser.userservice.shared.service.messageBroker;//package com.xbankuser.userservice.shared.service.messageBroker;
//
import com.rabbitmq.client.*;
import com.xbankuser.userservice.shared.service.emailClient.BaseEmailClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class Receiver {
    private final BaseEmailClient emailClient;
    public static final String EMAIL_DLX_ROUTING_KEY = "EMAIL_DLX_ROUTING_KEY";
    public static final String EMAIL_QUEUE_EXCHANGE = "EMAIL_QUEUE_EXCHANGE";
    public static final String EMAIL_QUEUE_ROUTING_KEY = "EMAIL_QUEUE_ROUTING_KEY";
    private final static String QUEUE_NAME = "EMAIL_QUEUE";
    public static final String RETRY_QUEUE = "RETRY_QUEUE";
    public static final String MY_DLX = "MY_DLX";

    @Bean
    public Channel listenForEmailRetries() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            Map<String, Object> arg = new HashMap<>();
            arg.put("x-dead-letter-exchange", MY_DLX);
            arg.put("x-dead-letter-routing-key", EMAIL_DLX_ROUTING_KEY);

            channel.exchangeDeclare(MY_DLX, BuiltinExchangeType.DIRECT);

            channel.queueDeclare(QUEUE_NAME, false, false, false, arg);

            channel.exchangeDeclare(EMAIL_QUEUE_EXCHANGE, BuiltinExchangeType.DIRECT);

            channel.queueBind(QUEUE_NAME, EMAIL_QUEUE_EXCHANGE, EMAIL_QUEUE_ROUTING_KEY);

            Map<String, Object> arg2 = new HashMap<>();
            arg2.put("x-message-ttl", 30000);
            arg2.put("x-dead-letter-exchange", EMAIL_QUEUE_EXCHANGE);
            arg2.put("x-dead-letter-routing-key", EMAIL_QUEUE_ROUTING_KEY);


            channel.queueDeclare(RETRY_QUEUE, true, false, false, arg2);
            channel.queueBind(RETRY_QUEUE, MY_DLX, EMAIL_DLX_ROUTING_KEY);

            System.out.println("Waiting for messages from failed emails...........................");
            boolean autoAck = false;

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                try {
                    System.out.println("Received email message for retry: '" + message + "'");

                    String[] emailDetails = message.split("\\|");
                    String recipientEmail = emailDetails[1];
                    String subject = emailDetails[2];
                    String emailBody = emailDetails[3];

                    try {
                        this.emailClient.sendEmail(recipientEmail, subject, emailBody);
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    } catch (Exception e) {
                        System.err.println("Failed to resend email. Keeping in the queue: " + e.getMessage());
                        channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
                    }
                } finally {
                    System.out.println(" [x] Done");
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };

            channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {});
            return channel;
        }
    }

    @Bean
    public static Channel RetrierForQueueFailed() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        try(        Connection connection = connectionFactory.newConnection();
                    Channel channel = connection.createChannel()
        ) {
            System.out.println("Waiting for failed messages from Email Queue.");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());

                System.out.println("Received message: " + message + " ");
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                var ackMultipleDeliveries = false;

                String[] emailDetails = message.split("\\|");

                emailDetails[0] = String.valueOf(Integer.parseInt(emailDetails[0]) + 1);

                message = String.join("\\|", emailDetails);

                channel.basicPublish(EMAIL_QUEUE_EXCHANGE, EMAIL_QUEUE_ROUTING_KEY, null, message.getBytes());

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), ackMultipleDeliveries);
            };

            channel.basicConsume(RETRY_QUEUE, false, deliverCallback, consumerTag -> {});


            return channel;
        }
    }
}