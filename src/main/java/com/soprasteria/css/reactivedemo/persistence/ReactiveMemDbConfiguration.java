package com.soprasteria.css.reactivedemo.persistence;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;


@Configuration
@EnableR2dbcRepositories
@ConditionalOnProperty(name = "demo.database", havingValue = "H2", matchIfMissing = true)
public class ReactiveMemDbConfiguration extends AbstractR2dbcConfiguration {

    private final String dbName;

    public ReactiveMemDbConfiguration(@Value("spring.r2dbc.name:demo") String dbName) {
        this.dbName = dbName;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get("r2dbc:h2:mem:///" + dbName);
    }
}
