package com.xbankuser.userservice.shared.service.emailClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SibClient {
    @Value("${sib.apikey}")
    private String sibApiKey;

    @Value("${sib.sender}")
    private String emailSender;

    @Value("${sib.senderName}")
    private String emailSenderName;

    protected void initiate(String recipientEmail, String subject, String message) {
        String url = "https://api.brevo.com/v3/smtp/email";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("api-key", sibApiKey);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("sender", Map.of("name", emailSenderName, "email", emailSender));
        requestBody.put("to", List.of(Map.of("email", recipientEmail, "name", "X-BANK")));
        requestBody.put("htmlContent", "<!DOCTYPE html> <html> <body> <h1>"+ message+ "</h1> </body> </html>");
        requestBody.put("subject", subject);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        try {
            ResponseEntity<String> response= restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

            System.out.println(response);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}