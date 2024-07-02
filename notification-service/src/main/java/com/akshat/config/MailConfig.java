package com.akshat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    /**
     * Here we are mocking the JavaMailSender bean for testing purposes, as we don't have a
     * smtp configuration here yet
     */

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl() {
            @Override
            public void send(SimpleMailMessage simpleMessage) throws MailException {
                // Mock implementation: do nothing
                System.out.println("Mock mail sending: " + simpleMessage.toString());
            }

            // we can override other send methods similarly if needed
        };
    }
}

