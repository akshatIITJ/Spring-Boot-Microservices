package com.akshat.order_service.controller;

import com.akshat.order_service.dto.OrderRequestDTO;
import com.akshat.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;


    private final String fallbackErrorMessage = "Oops! Something went wrong, please try again later!";
    private final String rateLimiterFallbackErrorMessage = "Rate limit reached, please try later!";
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethodCircuitBreaker")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    @RateLimiter(name = "inventory", fallbackMethod = "fallbackMethodRateLimiter")
    public CompletableFuture<ResponseEntity<String>> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        return CompletableFuture.supplyAsync(() ->
                orderService.placeOrder(orderRequestDTO));
    }

    /**
     * The fallback method below takes the same arguments as the placeOrder method, plus an additional argument
     * runtimeException. Also, the return type is same as the placeOrder method.
     * @param orderRequestDTO
     * @param runtimeException
     * @return String
     */
    public CompletableFuture<String> fallbackMethodCircuitBreaker(OrderRequestDTO orderRequestDTO, RuntimeException runtimeException) {
        log.debug("Error message : {}", runtimeException.getMessage());
        return CompletableFuture.supplyAsync(() -> fallbackErrorMessage);
    }

    public CompletableFuture<String> fallbackMethodRateLimiter(OrderRequestDTO orderRequestDTO, RuntimeException runtimeException) {
        log.debug("Error message : {}", runtimeException.getMessage());
        return CompletableFuture.supplyAsync(() -> rateLimiterFallbackErrorMessage);
    }

}
