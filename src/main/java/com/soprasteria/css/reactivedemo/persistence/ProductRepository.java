package com.soprasteria.css.reactivedemo.persistence;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ProductRepository extends R2dbcRepository<ProductEntity, UUID> {

    Flux<ProductEntity> findProductEntitiesByDescriptionContains(String description);

    Flux<ProductEntity> findAllByName(String name);
}
