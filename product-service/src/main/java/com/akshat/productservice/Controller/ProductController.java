package com.akshat.productservice.Controller;

import com.akshat.productservice.DTO.ProductDTO;
import com.akshat.productservice.DTO.ProductResponseDTO;
import com.akshat.productservice.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductResponseDTO productResponseDTO = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);

    }

    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> productResponseDTOList = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTOList);
    }
}
