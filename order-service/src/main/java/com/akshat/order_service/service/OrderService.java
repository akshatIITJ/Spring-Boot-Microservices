package com.akshat.order_service.service;

import com.akshat.order_service.dto.InventoryResponseDTO;
import com.akshat.order_service.dto.OrderLineItemsDTO;
import com.akshat.order_service.dto.OrderRequestDTO;
import com.akshat.order_service.event.OrderPlacedEvent;
import com.akshat.order_service.model.Order;
import com.akshat.order_service.model.OrderLineItems;
import com.akshat.order_service.repository.OrderRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final ObservationRegistry observationRegistry;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    private final String orderPlacedSuccessfully = "Order placed successfully !";
    private final String productNotInStock = "Product is not in stock, please try again later";

    public ResponseEntity<String> placeOrder(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequestDTO.getOrderLineItemsDTOList().stream().map(
                this::mapToOrderLineItems
        ).toList();

        order.setOrderLineItemsList(orderLineItemsList);

        // create a list of all skuCodes from orderLineItemsList
        // to send into webClient request being sent to Inventory Service
        List<String> skuCodesList = orderLineItemsList.stream().map(OrderLineItems::getSkuCode).toList();


        // Call inventory service, and place order if product is in stock
        Observation inventoryServiceObservation = Observation.createNotStarted("inventory-service-lookup", observationRegistry);
        inventoryServiceObservation.lowCardinalityKeyValue("call", "inventory-service");
        return inventoryServiceObservation.observe(() -> {
            List<InventoryResponseDTO> inventoryResponseDTOList = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodesList).build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<InventoryResponseDTO>>() {}) // this line tells to deserialize the response body to List<InventoryResponseDTO>
                    .block(); // block() so that webClient makes a sync request
                            // webClient makes async request by default

            // new inventoryResponseMap logic below
            Map<String, InventoryResponseDTO> inventoryResponseMap = inventoryResponseDTOList.stream()
                    .collect(Collectors.toMap(InventoryResponseDTO::getSkuCode, inventoryResponseDTO -> inventoryResponseDTO));

            // check if all products are in stock
//            Boolean allProductsInStock = inventoryResponseDTOList.stream().allMatch(
//                    inventoryResponseDTO -> inventoryResponseDTO.getIsInStock());

            // new check for all products in stock
            Boolean allProductsInStock = orderLineItemsList.stream().allMatch(orderLineItem -> {
                InventoryResponseDTO inventoryResponse = inventoryResponseMap.get(orderLineItem.getSkuCode());
                if (inventoryResponse == null) {
                    return false;
//                    throw new IllegalArgumentException("Product not found in inventory");
                }
                return inventoryResponse.getIsInStock() && inventoryResponse.getQuantity() >= orderLineItem.getQuantity();
            });

            if (allProductsInStock) {
                orderRepository.save(order);
                kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
                return ResponseEntity.status(HttpStatus.CREATED).body(orderPlacedSuccessfully);
//                return orderPlacedSuccessfully;
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productNotInStock);
//            throw new IllegalArgumentException("Product is not in stock, please try again later");
            }
        });

        // testing lines start
//        List<InventoryResponseDTO> inventoryResponseDTOList = webClientBuilder.build().get()
//                .uri("http://inventory-service/api/inventory",
//                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodesList).build())
//                .retrieve()
//                .bodyToMono(new ParameterizedTypeReference<List<InventoryResponseDTO>>() {}) // this line tells to deserialize the response body to List<InventoryResponseDTO>
//                .block(); // block() so that webClient makes a sync request
//        // webClient makes async request by default
//
//        // new inventoryResponseMap logic below
//        Map<String, InventoryResponseDTO> inventoryResponseMap = inventoryResponseDTOList.stream()
//                .collect(Collectors.toMap(InventoryResponseDTO::getSkuCode, inventoryResponseDTO -> inventoryResponseDTO));
//
//        // check if all products are in stock
////            Boolean allProductsInStock = inventoryResponseDTOList.stream().allMatch(
////                    inventoryResponseDTO -> inventoryResponseDTO.getIsInStock());
//
//        // new check for all products in stock
//        Boolean allProductsInStock = orderLineItemsList.stream().allMatch(orderLineItem -> {
//            InventoryResponseDTO inventoryResponse = inventoryResponseMap.get(orderLineItem.getSkuCode());
//            if (inventoryResponse == null) {
//                return false;
////                    throw new IllegalArgumentException("Product not found in inventory");
//            }
//            return inventoryResponse.getIsInStock() && inventoryResponse.getQuantity() >= orderLineItem.getQuantity();
//        });
//
//        if (allProductsInStock) {
//            orderRepository.save(order);
//            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
//            return ResponseEntity.status(HttpStatus.CREATED).body(orderPlacedSuccessfully);
//        }
//        else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productNotInStock);
////            throw new IllegalArgumentException("Product is not in stock, please try again later");
//        }
        // testing lines end

    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsDTO orderLineItemsDTO) {
        return OrderLineItems.builder().skuCode(orderLineItemsDTO.getSkuCode())
                .price(orderLineItemsDTO.getPrice()).quantity(orderLineItemsDTO.getQuantity()).build();
    }
}


