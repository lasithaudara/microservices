package com.lasitha.practice.productservice.service;

import com.lasitha.practice.productservice.model.Product;
import com.lasitha.practice.productservice.model.ProductRequest;
import com.lasitha.practice.productservice.model.ProductResponse;
import com.lasitha.practice.productservice.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    private ProductService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ProductService(productRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createProduct() {
        //given
        ProductRequest productReq = ProductRequest.builder()
                .name("iphone13")
                .description("latest iphone available")
                .price(BigDecimal.valueOf(500000))
                .build();

        Product product = Product.builder()
                .name(productReq.name())
                .description(productReq.description())
                .price(productReq.price())
                .build();
        //when
        underTest.createProduct(productReq);

        //then
        ArgumentCaptor<Product> productArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());

        Product capturedStudent = productArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(product);
    }

    @Test
    void checkProduct() {
        //given
        ProductRequest productReq = ProductRequest.builder()
                .name("iphone13")
                .description("latest iphone available")
                .price(BigDecimal.valueOf(500000))
                .build();

        Product product = Product.builder()
                .name(productReq.name())
                .description(productReq.description())
                .price(productReq.price())
                .build();

        given(productRepository.findByName(productReq.name())).willReturn(Optional.of(product));

        //when
        //then
        assertThatThrownBy(()->underTest.createProduct(productReq))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product - "+productReq.name()+" is already present");
    }

    @Test
    void getAllProducts() {
        //when
        underTest.getAllProducts();
        //then
        verify(productRepository).findAll();
    }

    @Test
    void mapToDto() throws Exception {
        //given
        ProductRequest productReq = ProductRequest.builder()
                .name("iphone13")
                .description("latest iphone available")
                .price(BigDecimal.valueOf(500000))
                .build();

        Product product = Product.builder()
                .id("1")
                .name(productReq.name())
                .description(productReq.description())
                .price(productReq.price())
                .build();

        ProductResponse productRes = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();

        //when
        Method mapToDto = underTest.getClass()
                .getDeclaredMethod("mapToDto", Product.class);
        mapToDto.setAccessible(true);

        ProductResponse invokeObject = (ProductResponse) mapToDto.invoke(underTest, product);

        //then
        assertThat(invokeObject).isEqualTo(productRes);

    }
}