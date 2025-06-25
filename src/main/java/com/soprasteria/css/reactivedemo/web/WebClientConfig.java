package com.soprasteria.css.reactivedemo.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    private final String quoteApiUrl;

    public WebClientConfig(@Value("${quote.host:http://api.quotable.io}") String quoteApiUrl) {
        this.quoteApiUrl = quoteApiUrl;
    }

    @Bean
    public WebClient webClient(HttpClient httpClient) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public WebClient quoteClient() {
        return WebClient.create(this.quoteApiUrl);
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create();
    }
}
