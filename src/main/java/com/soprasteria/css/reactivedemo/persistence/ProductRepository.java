package com.soprasteria.css.reactivedemo.persistence;

import com.soprasteria.css.reactivedemo.persistence.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends R2dbcRepository<ProductEntity, UUID> {

    Flux<ProductEntity> findProductEntitiesByDescriptionContains(String description);

    // this is not useful because we defined the name column unique
    // Flux<ProductEntity> findAllByName(String name);
    // instead:
    Mono<ProductEntity> findByName(String name);
}
