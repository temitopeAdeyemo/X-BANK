package com.xbankuser.userservice.shared.service.emailClient;

import com.xbankuser.userservice.shared.service.messageBroker.Publisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaseEmailClient {
    private final SibClient sibClient;
    private final Publisher publisher;

    public void sendEmail(String recipientEmail, String subject, String message) {
        try {
            sibClient.initiate(recipientEmail, subject, message);
        } catch (Exception e) {
            System.err.println("Error occurred while sending email. Queuing for retry.");
            queueFailedEmail(recipientEmail, subject, message);
        }
    }

    private void queueFailedEmail(String recipientEmail, String subject, String message) {
        String queueName = "EMAIL_QUEUE";
        try {
            // Create a message in the format you want (could be JSON or a simple concatenated string)
            String emailMessage = "0" + "|" + recipientEmail + "|" + subject + "|" + message;
            publisher.publishEmail(emailMessage);
        } catch (Exception e) {
            System.err.println("Failed to queue email for retry: " + e.getMessage());
            throw new RuntimeException("Failed to queue email for retry.", e);
        }
    }
}