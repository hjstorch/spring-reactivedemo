package com.soprasteria.css.reactivedemo.persistence;

import com.soprasteria.css.reactivedemo.persistence.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ProductRepository extends R2dbcRepository<ProductEntity, UUID> {

    Flux<ProductEntity> findProductEntitiesByDescriptionContains(String description);

    Flux<ProductEntity> findAllByName(String name);
}
