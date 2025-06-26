package com.soprasteria.css.reactivedemo.product;

import com.soprasteria.css.reactivedemo.persistence.entity.ProductEntity;
import com.soprasteria.css.reactivedemo.persistence.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);

    private ProductController sut;

    @BeforeEach
    public void setUp() {
        when(productRepository.findAll())
                .thenReturn(Flux.just(new ProductEntity(UUID.randomUUID()),
                        new ProductEntity(UUID.randomUUID())));

        doAnswer( in ->
                Mono.defer( () -> {
                    ProductEntity entity = new ProductEntity();
                    entity.setId(in.getArgument(0));
                    return Mono.just(entity);
                })
        ).when(productRepository).findById(any(UUID.class));

        sut = new ProductController(productRepository);
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
        UUID uuid = UUID.randomUUID();
        sut.getProduct(uuid)
                .as(StepVerifier::create)
                .expectNextMatches( product ->
                        product.getId().equals(uuid)
                )
                .expectComplete()
                .verify();
    }
}
