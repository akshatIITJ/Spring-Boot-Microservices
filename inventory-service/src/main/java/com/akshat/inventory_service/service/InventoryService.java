package com.akshat.inventory_service.service;

import com.akshat.inventory_service.dto.InventoryResponseDTO;
import com.akshat.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponseDTO> isInStock(List<String> skuCode) {
         return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                 .map(inventory -> InventoryResponseDTO.builder().skuCode(inventory.getSkuCode())
                         .isInStock(inventory.getQuantity() > 0).quantity(inventory.getQuantity()).build()).toList();
    }
}
