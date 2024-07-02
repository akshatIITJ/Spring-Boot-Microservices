package com.akshat.kafkaListener;

import com.akshat.event.OrderPlacedEvent;
import com.akshat.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.akshat.constants.EmailConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {
    private final EmailService emailService;

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        log.info("Received notification for order - {}", orderPlacedEvent.getOrderNumber());
        // send out email here
        String emailId = testingEmailId;
        String subject = orderPlaced;
        String message = String.format(emailMessage, orderPlacedEvent.getOrderNumber());

        // sending out email
        emailService.sendEmail(emailId, subject, message);
        log.info("Sent notification email for order ID: {}", orderPlacedEvent.getOrderNumber());
    }
}
