package com.soprasteria.css.reactivedemo.greeting;

import com.soprasteria.css.reactivedemo.quote.Quote;
import com.soprasteria.css.reactivedemo.quote.QuoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

public class GreetingServiceTest {

    public static final String SOMETHING_WISE_QUOTE = "something wise";
    private GreetingService greetingService;

    private QuoteService quoteService = Mockito.mock(QuoteService.class);

    @BeforeEach
    public void setUp() {
        Quote quote = new Quote();
        quote.setContent(SOMETHING_WISE_QUOTE);
        when(this.quoteService.getQuote()).thenReturn(Mono.just(quote));

        this.greetingService = new GreetingService(quoteService, "Hello");
    }

    @Test
    public void greetingTest() {

        StepVerifier.create(greetingService.createGreeting(Mono.just("User")))
                .expectNextMatches( greet ->
                        greet.getGreeting().equals("Hello User") &&
                                greet.getQuote().equals(SOMETHING_WISE_QUOTE)
                        )
                .verifyComplete(); // short for .expectComplete().verify()
    }

    @Test
    public void greetingWithEmptyUserTest() {
        StepVerifier.create(greetingService.createGreeting(Mono.empty()))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    public void greetingWithEmptyUserNameTest() {
        StepVerifier.create(greetingService.createGreeting(Mono.just("")))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

}
