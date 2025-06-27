package com.soprasteria.css.reactivedemo.product.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Product {

    private String name;
    private String description;
    private BigDecimal price;
}
