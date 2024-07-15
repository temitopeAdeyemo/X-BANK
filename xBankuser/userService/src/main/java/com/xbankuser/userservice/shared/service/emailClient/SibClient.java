package com.xbankuser.userservice.shared.service.emailClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;
import java.util.List;

@Service
public class SibClient {
    @Value("${sib.apikey}")
    private static String sibApiKey;

    @Value("${sib.sender}")
    private static String emailSender;

    @Value("${sib.senderName}")
    private static String emailSenderName;

    public void sendEmail(String recipientEmail, String subject, String message, String name) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();

        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");

        apiKey.setApiKey(sibApiKey);

        TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();

        SendSmtpEmail email = getSendSmtpEmail( recipientEmail, subject, message, name);

        try {
            CreateSmtpEmail response = apiInstance.sendTransacEmail(email);
            System.out.println(response);
        } catch (ApiException e) {
            System.err.println("Exception when calling TransactionalEmailsApi#sendTransacEmail");
            e.printStackTrace();
        }
    }

    @NotNull
    private static SendSmtpEmail getSendSmtpEmail(String recipientEmail, String subject, String message, String name) {
        SendSmtpEmailSender sender = new SendSmtpEmailSender();
        sender.setEmail(emailSender);
        sender.setName(emailSenderName);

        SendSmtpEmailTo recipient = new SendSmtpEmailTo();
        recipient.setEmail(recipientEmail);
        recipient.setName(name.isEmpty()? "X-BANK-USER": name);

        SendSmtpEmail email = new SendSmtpEmail();
        email.setSender(sender);
        email.setTo(List.of(recipient));
        email.setSubject(subject);
        email.setHtmlContent("<html><body><h1>"+message+"</h1></body></html>");
        return email;
    }
}