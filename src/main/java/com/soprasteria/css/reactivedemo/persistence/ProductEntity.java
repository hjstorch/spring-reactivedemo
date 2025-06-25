package com.soprasteria.css.reactivedemo.persistence;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Table(name = "PRODUCT")
public class ProductEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;

    @Override
    public boolean isNew() {
        if(null == id) {
            this.setId(UUID.randomUUID());
            return true;
        }
        return false;
    }
}
