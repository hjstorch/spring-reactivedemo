package com.soprasteria.css.reactivedemo.greeting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping(value = {"/hello/", "/hello/{name}"})
    public Mono<Greeting> greet(@PathVariable(required = false) String name,
                                @RequestAttribute(required = false) String myname,
                                @RequestAttribute(required = false) boolean badname) {
        return greetingService.createGreeting(Mono.justOrEmpty(name))
                .doOnError( ex -> log.error("an error occurred at greeting name", ex))
                .onErrorResume(e ->
                        greetingService.createGreeting(
                                Mono.justOrEmpty( badname ? myname : "World")
                        )
                );
    }
}
