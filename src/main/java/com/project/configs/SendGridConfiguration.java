package com.project.configs;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfiguration {
    @Value("${spring.sendgrid.api-key}")
    private String sendGridAPIKey;

    @Bean
    public SendGrid getSendGrid() {
        return new SendGrid(sendGridAPIKey);
    }
}
