package com.soprasteria.css.reactivedemo.greeting;

import com.soprasteria.css.reactivedemo.greeting.model.Greeting;
import com.soprasteria.css.reactivedemo.greeting.service.GreetingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GreetingControllerTest {

    private GreetingService greetingService = Mockito.mock(GreetingService.class);

    private GreetingController sut;

    @BeforeEach
    public void setUp() {
        when(greetingService.createGreeting(any(Mono.class)))
                .thenAnswer( in -> {
                    // this is kind of odd but right now there is no other way to do
                    Mono<String> parameter = in.getArgument(0, Mono.class);
                    return parameter
                            .switchIfEmpty(Mono.error(IllegalArgumentException::new))
                            .map(s -> Greeting.builder()
                                    .greeting("Hello " + s)
                                    .quote("something wise")
                                    .build());
                });

        sut = new GreetingController(greetingService);
    }


    @Test
    public void testNormal() {

        sut.greet("User", null, false)
                .as(StepVerifier::create)
                .expectNextMatches( greet ->
                        greet.getGreeting().equals("Hello User")
                )
                .expectComplete()
                .verify();
    }

    @Test
    public void testBadName() {

        sut.greet(null, "User", true)
                .as(StepVerifier::create)
                .expectNextMatches( greet ->
                        greet.getGreeting().equals("Hello User")
                )
                .expectComplete()
                .verify();

        verify(greetingService, times(2)).createGreeting(any(Mono.class));
    }

    @Test
    public void testIgnoreUriParameterIfNoBadName() {

        sut.greet(null, "User", false)
                .as(StepVerifier::create)
                .expectNextMatches( greet ->
                        greet.getGreeting().equals("Hello World")
                )
                .expectComplete()
                .verify();

        verify(greetingService, times(2)).createGreeting(any(Mono.class));
    }

    @Test
    public void testEmpty() {

        sut.greet(null, null, false)
                .as(StepVerifier::create)
                .expectNextMatches( greet ->
                        greet.getGreeting().equals("Hello World")
                )
                .expectComplete()
                .verify();

        verify(greetingService, times(2)).createGreeting(any(Mono.class));
    }
}
