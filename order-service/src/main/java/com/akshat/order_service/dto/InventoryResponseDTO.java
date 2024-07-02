package com.akshat.order_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class InventoryResponseDTO {
    private String skuCode;
    private Boolean isInStock;
    private Integer quantity;
}
