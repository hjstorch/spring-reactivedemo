package com.soprasteria.css.reactivedemo.greeting;

import com.soprasteria.css.reactivedemo.quote.Quote;
import com.soprasteria.css.reactivedemo.quote.QuoteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Service
public class GreetingService {

    private final QuoteService quoteService;
    private final String greet;

    public GreetingService(QuoteService quoteService, @Value("${demo.greeting}") String greet) {
        this.quoteService = quoteService;
        this.greet = greet;
    }

    public Mono<Greeting> createGreeting(Mono<String> name) {
        return name
                .switchIfEmpty( Mono.error(new IllegalArgumentException()))
                .mapNotNull(givenName -> {
                    if (StringUtils.hasText(givenName))
                        return greet + " " + givenName;
                    else
                        throw new IllegalArgumentException();
                })
                .zipWhen(greeting -> quoteService.getQuote(),
                        (greeting, quote) ->
                                Greeting.builder()
                                        .greeting(greeting)
                                        .quote(quote.getContent())
                                        .build()
                );
    }
}
