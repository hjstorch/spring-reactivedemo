package com.soprasteria.css.reactivedemo.product;

import com.soprasteria.css.reactivedemo.product.model.Product;
import com.soprasteria.css.reactivedemo.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    private final ProductService productService = mock(ProductService.class);

    private ProductController sut;

    @BeforeEach
    public void setUp() {
        when(productService.listProducts())
                .thenReturn(Flux.just(
                        Product.builder().name("P2").build(),
                        Product.builder().name("P2").build()));

        doAnswer( in ->
                Mono.defer( () -> {
                    Product p = Product.builder().name(in.getArgument(0)).build();
                    return Mono.just(p);
                })
        ).when(productService).getProduct(any(String.class));

        sut = new ProductController(productService);
    }

    @Test
    public void listProductsTest() {
        sut.listProducts()
                .as(StepVerifier::create)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    public void getProductTest() {
        String name = "P1";
        sut.getProduct(name)
                .as(StepVerifier::create)
                .expectNextMatches( product ->
                        product.getName().equals(name)
                )
                .expectComplete()
                .verify();
    }

    @Test
    public void getProductErrorTest() {

        when(productService.getProduct(any(String.class))).thenReturn(Mono.empty());

        String name = "P1";
        sut.getProduct(name)
                .as(StepVerifier::create)
                .expectError(NoSuchElementException.class)
                .verify();
    }

    @Test
    public void getProductsErrorTest() {

        when(productService.listProducts()).thenReturn(Flux.empty());

        sut.listProducts()
                .as(StepVerifier::create)
                .expectError(NoSuchElementException.class)
                .verify();
    }
}
