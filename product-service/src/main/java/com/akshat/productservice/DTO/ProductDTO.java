package com.akshat.productservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/** This class is for incoming requests into the ProductController class.
 *  The request body is converted from json to ProductDTO at the controller level itself,
 *  by deserialization which Spring Boot does for us.
 */
public class ProductDTO {
    private String name;
    private String description;
    private BigDecimal price;
}
