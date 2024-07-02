package com.akshat.inventory_service.controller;

import com.akshat.inventory_service.dto.InventoryResponseDTO;
import com.akshat.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;
    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> isInStock(@RequestParam List<String> skuCode) {
        return ResponseEntity.ok(inventoryService.isInStock(skuCode));
    }
}
