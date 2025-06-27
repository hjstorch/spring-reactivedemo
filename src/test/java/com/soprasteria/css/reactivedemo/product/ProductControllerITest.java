package com.soprasteria.css.reactivedemo.product;

import com.soprasteria.css.reactivedemo.exception.WebExceptionHandler;
import com.soprasteria.css.reactivedemo.persistence.ProductRepository;
import com.soprasteria.css.reactivedemo.persistence.entity.ProductEntity;
import com.soprasteria.css.reactivedemo.product.model.Product;
import com.soprasteria.css.reactivedemo.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@WebFluxTest(controllers = ProductController.class,
        properties = {
                "demo.greeting=Guude,",
                "demo.database=H2"
        }
)
@AutoConfigureWebTestClient
@Import({ProductService.class, WebExceptionHandler.class})
@ActiveProfiles("test")
public class ProductControllerITest {

    @Autowired
    WebTestClient client;

    @MockitoBean
    ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        when(productRepository.findAll())
                .thenReturn(Flux.just(
                        new ProductEntity(UUID.randomUUID()),
                        new ProductEntity(UUID.randomUUID())));

        doAnswer( in ->
                Mono.defer( () -> {
                    ProductEntity p = new ProductEntity(UUID.randomUUID());
                    p.setName(in.getArgument(0));
                    return Mono.just(p);
                })
        ).when(productRepository).findByName(any(String.class));
    }

    @Test
    @WithMockUser
    public void productsTest() {
        client.get().uri("/product")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(List.class)
                .consumeWith(result -> {
                    List<Product> products = result.getResponseBody();
                    assertNotNull("", products);
                    assertEquals("", 2, products.size());
                })
                .returnResult();

        verify(productRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser
    public void productByNameTest() {
        client.get().uri("/product/P1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Product.class)
                .consumeWith(result -> {
                    Product product = result.getResponseBody();
                    assertNotNull("", product);
                    assertNotNull("", product.getName());
                    assertEquals("", "P1", product.getName());
                })
                .returnResult();

        verify(productRepository, times(1)).findByName("P1");
    }

    @Test
    @WithMockUser
    public void noProductsTest() {
        when(productRepository.findAll())
                .thenReturn(Flux.empty());

        client.get().uri("/product")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .consumeWith( result ->
                        assertEquals("", "no products found", result.getResponseBody())
                )
                .returnResult();

        verify(productRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser
    public void productWithNameNotExistsTest() {
        when(productRepository.findByName("P88")).thenReturn(Mono.empty());

        client.get().uri("/product/P88")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .consumeWith( result ->
                        assertEquals("", "P88 does not exist", result.getResponseBody())
                )
                .returnResult();

        verify(productRepository, times(1)).findByName("P88");
    }
}
