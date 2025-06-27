package com.soprasteria.css.reactivedemo.product.service;

import com.soprasteria.css.reactivedemo.persistence.ProductRepository;
import com.soprasteria.css.reactivedemo.persistence.entity.ProductEntity;
import com.soprasteria.css.reactivedemo.product.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Flux<Product> listProducts() {
        return productRepository.findAll()
                .map(this::mapProduct)
                .switchIfEmpty(Flux.empty());
    }

    public Mono<Product> getProduct(@PathVariable UUID id) {
        return productRepository.findById(id)
                .map(this::mapProduct)
                .switchIfEmpty(Mono.empty());
    }

    public Mono<Product> getProduct(String name) {
        return productRepository.findByName(name)
                .map(this::mapProduct)
                .switchIfEmpty(Mono.empty());
    }

    private Product mapProduct(ProductEntity entity) {
        return Product.builder()
                .name(entity.getName())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .build();
    }
}
