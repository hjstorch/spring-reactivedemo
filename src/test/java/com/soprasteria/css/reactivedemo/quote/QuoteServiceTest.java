package com.soprasteria.css.reactivedemo.quote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class QuoteServiceTest {

    private QuoteService quoteService;

    private WebClient webClient = mock(WebClient.class);
    private WebClient.RequestHeadersUriSpec requestHeaderUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
    private WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
    private WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

    @BeforeEach
    public void init() {
        when(webClient.get()).thenReturn(requestHeaderUriSpec);
        when(requestHeaderUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(MediaType.TEXT_EVENT_STREAM)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(Quote.class)).thenReturn(Flux.just(new Quote("First Quote","")));

        this.quoteService = new QuoteService(webClient, webClient, "", ""  );
    }

    @Test
    void testGetRandomQuote() {
        quoteService.getQuote()
                .as(StepVerifier::create)
                .expectNextMatches( m ->
                        m.getContent().equals("First Quote")
                )
                .expectComplete()
                .verify();
    }
}
