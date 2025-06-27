package com.soprasteria.css.reactivedemo.persistence;

import com.soprasteria.css.reactivedemo.persistence.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@DataR2dbcTest(
        properties = {
                "spring.r2dbc.url=r2dbc:h2:mem:///demo",
                "spring.r2dbc.password=demo",
                "spring.r2dbc.name=demo",
                "spring.r2dbc.user=demo",
                "spring.sql.init.mode=always",
                "spring.sql.init.data-locations=classpath:testsql/insert.sql",
                "demo.database=H2"
        }
)
//@Sql(scripts = {"classpath:testsql/insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS) // no support with r2dbc yet
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void testRead(){
        productRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
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
