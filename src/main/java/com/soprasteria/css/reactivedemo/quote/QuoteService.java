package com.soprasteria.css.reactivedemo.quote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class QuoteService {

    private final WebClient webClient;
    private final WebClient quoteClient;
    private final String quoteApi;
    private final String quotePath;

    @Autowired
    public QuoteService(WebClient webClient,
                        WebClient quoteClient,
                        @Value("${quote.host:http://api.quotable.io}") String quoteApi,
                        @Value("${quote.path:/quotes/random}") String quotePath) {
        this.webClient = webClient;
        this.quoteClient = quoteClient;
        this.quoteApi = quoteApi;
        this.quotePath = quotePath;
    }

    /**
     * Retrieve multiple quotes as streaming source
     * @return
     */
    public Flux<Quote> getQuotes() {
        return getRequestHeadersSpec()
                .accept(MediaType.TEXT_EVENT_STREAM) // get non blocking stream response
                .retrieve()
                .bodyToFlux(Quote.class);
    }

    public Mono<Quote> getQuote() {
        return getQuotes()
                .elementAt(0, new Quote());
    }

    /**
     * use exchange() to be more flexible in handling the response
     * @return
     */
    public Mono<Quote> exchangeQuote() {
        return getRequestHeadersSpec()
                .accept(MediaType.TEXT_EVENT_STREAM)// get non blocking stream response
                .exchangeToFlux( clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                        return clientResponse.bodyToFlux(Quote.class);
                    }
                    else {
                       // Turn to error
                        return Flux.empty();
                    }
                })
                .elementAt(0, new Quote());
    }

    private WebClient.RequestHeadersSpec<?> getRequestHeadersSpec() {
        return webClient.get().uri(quoteApi + quotePath);
    }

    private WebClient.RequestHeadersSpec<?> getQuoteRequestHeadersSpec() {
        return quoteClient.get().uri(quotePath); // quoteClient has baseUrl set
    }
}
