package com.lasitha.practice.notification;

import com.lasitha.practice.notification.consumer.event.OrderPlacedEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@EnableDiscoveryClient
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("-------------------------"+orderPlacedEvent.orderNumber()+ "--------------------------");
        System.out.println("---------------------------------------------------------------------------");
    }
}
