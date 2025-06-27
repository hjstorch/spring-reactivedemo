package com.soprasteria.css.reactivedemo.quote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soprasteria.css.reactivedemo.quote.model.Quote;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        properties = {
                "quote.host=http://localhost:9999",
                "quote.path=/quotes"
        }
)
public class QuoteServiceITest {

    public static MockWebServer mockBackEnd;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private QuoteService quoteService;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start(9999);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void testGetRandomQuote() throws JsonProcessingException, InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(mapper.writeValueAsString(new Quote("First Quote","")))
        );
        quoteService.getQuote()
                .as(StepVerifier::create)
                .expectNextMatches( quote -> quote.getContent().equals("First Quote"))
                .verifyComplete();

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals("/quotes", recordedRequest.getPath());
    }

}
