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
    public static final String EMAIL_DLX_ROUTING_KEY = "EMAIL_DLX_ROUTING_KEY";
    public static final String EMAIL_QUEUE_EXCHANGE = "EMAIL_QUEUE_EXCHANGE";
    public static final String EMAIL_QUEUE_ROUTING_KEY = "EMAIL_QUEUE_ROUTING_KEY";
    private final BaseEmailClient emailClient;
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






//
////    private void listenForEmailRetries(){
//////        Channel channel = createReceiver("EMAIL_QUEUE", );
////
////        try {
////            DeliverCallback deliverCallbackk = (consumerTag, delivery) -> {
////                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
////
////                System.out.println(" [x] Received '" + message + "'");
////                try {
////                    String messageFromQueue = new String(delivery.getBody(), "UTF-8");
////                    System.out.println(" [x] Received email message for retry: '" + message + "'");
////
////                    String[] emailDetails = message.split("\\|");
////                    String recipientEmail = emailDetails[0];
////                    String subject = emailDetails[1];
////                    String emailBody = emailDetails[2];
////
////                    try {
////                        this.emailClient.sendEmail(recipientEmail, subject, emailBody);
////                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
////                    } catch (Exception e) {
////                        System.err.println("Failed to resend email. Keeping in the queue: " + e.getMessage());
////                    }
////                } finally {
////                    System.out.println(" [x] Done");
////                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
////                }
////            };
////
////        } catch (Exception e) {
////            System.err.println("Failed to create Email queue: " + e.getMessage());
////        }
////    }
//
////    @Bean
//    public String init() {
//        try {
//            System.out.println("::::::::::--------");
//            listenForEmailRetries();
//            System.out.println("Email retry listener active");
//        } catch (Exception e) {
//            System.err.println("Failed to start email retry listener: " + e.getMessage());
//        }
//        return "Email retry listener active";
//    }
//}
//
//import com.xbankuser.userservice.shared.service.emailClient.EmailClient;
//import org.springframework.stereotype.Service;
//import javax.annotation.PostConstruct;
//
//import com.rabbitmq.client.Channel;
//import com.xbankuser.userservice.config.Rabbitmq;
//import com.xbankuser.userservice.shared.service.emailClient.EmailClient;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class Receiver {
//    private final static String QUEUE_NAME = "EMAIL_QUEUE";
//    private final EmailClient emailClient;
//
//    @PostConstruct
//    public void init() {
//        try {
//            System.out.println(":::::::::::::::::::::::::::");
//            listenForEmailRetries();  // Automatically call this when the bean is initialized
//        } catch (Exception e) {
//            System.err.println("Failed to start email retry listener: " + e.getMessage());
//        }
//    }
//
//    public void listenForEmailRetries() throws Exception {
//        // Keep the channel open for continuous consumption
//        Channel channel = Rabbitmq.createChannel();
//
//        // Declare the queue
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        System.out.println(" [*] Waiting for failed email messages...");
//
//        // Consume messages without closing the channel
//        channel.basicConsume(QUEUE_NAME, false, (consumerTag, delivery) -> {
//            String message = new String(delivery.getBody(), "UTF-8");
//            System.out.println(" [x] Received email message for retry: '" + message + "'");
//
//            String[] emailDetails = message.split("\\|");
//            String recipientEmail = emailDetails[0];
//            String subject = emailDetails[1];
//            String emailBody = emailDetails[2];
//
//            try {
//                // Retry sending the email
//                emailClient.sendEmail(recipientEmail, subject, emailBody);
//                // Acknowledge message after successful retry
//                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//            } catch (Exception e) {
//                System.err.println("Failed to resend email. Keeping in the queue: " + e.getMessage());
//                // Do not acknowledge the message, so it stays in the queue for retry
//            }
//        }, consumerTag -> {
//            // Handle consumer cancellation
//        });
//    }
//}











//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//import com.rabbitmq.client.DeliverCallback;
//import com.xbankuser.userservice.config.Rabbitmq;
//import com.xbankuser.userservice.shared.service.emailClient.EmailClient;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.nio.charset.StandardCharsets;
//
//@Configuration
//@RequiredArgsConstructor
//public class Receiver {
//        private final EmailClient emailClient;
//    private final static String QUEUE_NAME = "A_QUEUE";
//
//    @Bean
//    public String execute() throws Exception {
//
//
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
////        System.out.println("*******************---RABBITMQ CONNECTION CREATED---**************************");
////        try (Connection connection = factory.newConnection();
////             Channel channel = connection.createChannel()) {
////            return channel;
////        }
//
//        try(Connection connection = factory.newConnection();
//    Channel channel = connection.createChannel()) {
//            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
//
//                System.out.println(" [x] Received '" + message + "'");
//                try {
//                    System.out.println(" [x] Received email message for retry: '" + message + "'");
//
//                    String[] emailDetails = message.split("\\|");
//                    String recipientEmail = emailDetails[0];
//                    String subject = emailDetails[1];
//                    String emailBody = emailDetails[2];
//
//                    try {
//                        this.emailClient.sendEmail(recipientEmail, subject, emailBody);
//                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//                    } catch (Exception e) {
//                        System.err.println("Failed to resend email. Keeping in the queue: " + e.getMessage());
//                    }
//                } finally {
//                    System.out.println(" [x] Done");
//                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//                }
//            };
//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//            System.out.println(" [*] Waiting for messages...........................");
//            boolean autoAck = false;
//            channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> { });
//            return "";
//        }
//    }
//}