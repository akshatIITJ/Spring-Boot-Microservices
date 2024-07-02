//package com.akshat.productservice;
//
//import com.akshat.productservice.DTO.ProductDTO;
//import com.akshat.productservice.Repository.ProductRepository;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.testcontainers.containers.MongoDBContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.math.BigDecimal;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@Testcontainers
//@AutoConfigureMockMvc
//class ProductServiceApplicationTests {
//
//	@Container
//	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
//	@Autowired
//	private MockMvc mockMvc;
//	@Autowired
//	private ObjectMapper objectMapper;
//	@Autowired
//	private ProductRepository productRepository;
//
//	@DynamicPropertySource
//	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
//		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
//	}
//
//	@Test
//	void shouldCreateProduct() {
//		ProductDTO productDTO = getDummyProductRequest();
//		String productRequestString = null;
//		try {
//			productRequestString = objectMapper.writeValueAsString(productDTO);
//		} catch (JsonProcessingException e) {
//			System.out.println("ObjectMapper threw a JsonProcessingException. Exception message : " + e.getMessage());
//		}
//
//		try {
//			mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(productRequestString))
//					.andExpect(status().isCreated());
//		} catch (Exception e) {
//			System.out.println("An exception was thrown. Exception message : " + e.getMessage());
//		}
//
//		Assertions.assertEquals(1, productRepository.findAll().size());
//
//	}
//
//	private ProductDTO getDummyProductRequest() {
//		return ProductDTO.builder().name("iPhone 14")
//				.description("iPhone 14 is the second latest smartphone from Apple")
//		.price(BigDecimal.valueOf(50000)).build();
//	}
//
//
//}
