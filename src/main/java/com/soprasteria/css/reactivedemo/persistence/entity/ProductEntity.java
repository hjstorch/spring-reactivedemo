package com.soprasteria.css.reactivedemo.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "PRODUCT")
public class ProductEntity implements Persistable<UUID> {

    @Id
    @NonNull
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
