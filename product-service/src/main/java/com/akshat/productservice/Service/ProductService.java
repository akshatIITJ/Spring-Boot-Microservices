package com.akshat.productservice.Service;

import com.akshat.productservice.DTO.ProductDTO;
import com.akshat.productservice.DTO.ProductResponseDTO;
import com.akshat.productservice.Model.Product;
import com.akshat.productservice.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    public ProductResponseDTO createProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Product {} is saved !", product.getId());

        ProductResponseDTO productResponseDTO = mapToProductResponseDTO(savedProduct);
        return productResponseDTO;
    }


    public List<ProductResponseDTO> getAllProducts() {
        List<ProductResponseDTO> productResponseDTOS = productRepository.findAll().stream().map(
                this::mapToProductResponseDTO
        ).toList();

        return productResponseDTOS;
    }

    private ProductResponseDTO mapToProductResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
