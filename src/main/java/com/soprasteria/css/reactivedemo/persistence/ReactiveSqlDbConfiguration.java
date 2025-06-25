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
@ConditionalOnProperty(name = "demo.database", havingValue = "SQL", matchIfMissing = false)
public class ReactiveSqlDbConfiguration extends AbstractR2dbcConfiguration {

    private final String dbUrl;

    public ReactiveSqlDbConfiguration(@Value("spring.r2dbc.url") String dbUrl) {
        this.dbUrl = dbUrl;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(dbUrl);
    }
}
