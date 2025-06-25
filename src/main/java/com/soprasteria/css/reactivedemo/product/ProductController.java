package com.soprasteria.css.reactivedemo.product;

import com.soprasteria.css.reactivedemo.persistence.ProductEntity;
import com.soprasteria.css.reactivedemo.persistence.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController("/product")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public Flux<ProductEntity> listProducts() {
        return productRepository.findAll()
                .switchIfEmpty(Flux.empty());
    }

    @GetMapping("/{id}")
    public Mono<ProductEntity> getProduct(@PathVariable UUID id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.empty());
    }
}
