package com.soprasteria.css.reactivedemo.persistence;

import com.soprasteria.css.reactivedemo.persistence.entity.ProductEntity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@DataR2dbcTest(
        properties = {
                "spring.r2dbc.url=r2dbc:tc:postgresql:///test?TC_IMAGE_TAG=17.5-alpine",
                "spring.r2dbc.password=test",
                "spring.r2dbc.user=test",
                "spring.r2dbc.initialization-mode=always",
                "spring.sql.init.mode=always",
                "spring.sql.init.schema-locations=classpath:sql/schema.sql",
                "spring.sql.init.data-locations=classpath:testsql/cleanup_postgres.sql, classpath:testsql/insert_postgres.sql",
                "demo.database=SQL"
        }
)
@Import(ReactiveSqlDbConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers()
class ProductRepositoryTCTest {

    @Autowired
    private ProductRepository productRepository;

    // it is not necessary to instanciate a Postgres Container
    // @Container
    // ...

    @Test
    @Disabled
    public void readFromRepositoryReturnsEntities(){
        productRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @Disabled
    public void testWriteAndDelete(){

        ProductEntity newEntity = new ProductEntity();
        newEntity.setName("P99");
        newEntity.setDescription("new Product");
        newEntity.setPrice(BigDecimal.TEN);

        productRepository.save(newEntity)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        productRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(3)
                .verifyComplete();

        productRepository.findByName("P99")
                .as(StepVerifier::create)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(Objects::nonNull)
                .consumeRecordedWith(e -> e.forEach(t -> productRepository.delete(t)))
                .verifyComplete();
    }
}
