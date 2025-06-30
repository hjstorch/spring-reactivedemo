package com.soprasteria.css.reactivedemo.greeting;

import com.soprasteria.css.reactivedemo.greeting.model.Greeting;
import com.soprasteria.css.reactivedemo.greeting.service.GreetingService;
import com.soprasteria.css.reactivedemo.quote.model.Quote;
import com.soprasteria.css.reactivedemo.quote.QuoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@WebFluxTest(controllers = GreetingController.class,
    properties = {
            "demo.greeting=Guude,",
            "demo.database=H2"
    }
)
@AutoConfigureWebTestClient
@Import({GreetingService.class})
@ActiveProfiles("test")
public class GreetingControllerITest {

    @Autowired
    WebTestClient client;

    @MockitoBean
    QuoteService quoteService;

    @BeforeEach
    public void setUp() {

        Quote quote = new Quote("something wise", "anonymous");

        when(quoteService.getQuote()).thenReturn(Mono.just(quote));
    }

    @Test
    @WithMockUser
    public void requestGreetingWithNoUserReturnsWorld() {
        client.get().uri("/hello/")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Greeting.class).consumeWith(result -> {
                    Greeting greeting = result.getResponseBody();
                    assertNotNull("", greeting);
                    assertNotNull("", greeting.getGreeting());
                    assertEquals("", "Guude, World", greeting.getGreeting());
                })
                .returnResult();

        verify(quoteService, times(1)).getQuote();
    }

    @Test
    @WithMockUser
    public void requestGreetingWithUserReturnsConfiguredGreeting() {
        client.get().uri("/hello/user")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Greeting.class).consumeWith( result -> {
                    Greeting greeting = result.getResponseBody();
                    assertNotNull("", greeting);
                    assertNotNull("", greeting.getGreeting());
                    assertEquals("", "Guude, user", greeting.getGreeting());
                })
                .returnResult();

        verify(quoteService, times(1)).getQuote();
    }
}
