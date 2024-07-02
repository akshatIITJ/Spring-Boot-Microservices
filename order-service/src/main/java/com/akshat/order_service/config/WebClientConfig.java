package com.akshat.order_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    /**
     To use this bean, we have to define the bean objects with the same name - "webClient",
     wherever we are using them in OrderService.
     Check OrderService for example.
     */
    @Bean
    @LoadBalanced // to enable client side load balancing for order service
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
