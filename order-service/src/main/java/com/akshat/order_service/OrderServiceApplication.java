package com.akshat.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderServiceApplication {
	// runs on port 8081

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
