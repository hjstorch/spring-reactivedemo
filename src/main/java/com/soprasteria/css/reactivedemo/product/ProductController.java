package com.soprasteria.css.reactivedemo.product;

import com.soprasteria.css.reactivedemo.product.model.Product;
import com.soprasteria.css.reactivedemo.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Flux<Product> listProducts() {
        return productService.listProducts()
                .switchIfEmpty(Mono.error(new NoSuchElementException("no products found")));
    }

    @GetMapping("{name}")
    public Mono<Product> getProduct(@PathVariable String name) {
        return productService.getProduct(name)
                .switchIfEmpty(Mono.error(new NoSuchElementException(name + " does not exist")));
    }
}
