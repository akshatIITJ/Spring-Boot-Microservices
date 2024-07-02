package com.akshat.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private List<OrderLineItemsDTO> orderLineItemsDTOList;
}
