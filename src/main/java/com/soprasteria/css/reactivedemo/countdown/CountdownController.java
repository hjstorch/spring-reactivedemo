package com.soprasteria.css.reactivedemo.countdown;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping(path = "/countdown/")
@CrossOrigin(origins = "${demo.cors.allowed-origins:*}")
public class CountdownController {

    @GetMapping(path = "sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CountData> getSse() {
        return streamSource2();
    }

    @GetMapping(path = "ndjson", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CountData> getNdJson() {
        return streamSource2();
    }

    @GetMapping(path = "streamerror", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<ResponseEntity<Flux<CountData>>> getSseResponse() {
        return Mono.just(ResponseEntity.ok(streamSource2()
                .doOnNext(CountdownController::abortAtSix)));
    }

    @GetMapping(path = "nostream")
    public Flux<CountData> getNoStream() {
        return streamSource2();
    }

    @GetMapping(path = "nostreamerror", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<CountData> getNoStreamError() {
        return streamSource2()
                .doOnNext(CountdownController::abortAtSix);
    }

    @GetMapping(path = "nostreamerrorresponse", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<CountData>>> getNoStreamErrorResponse() {
        return Mono.just(ResponseEntity.ok(streamSource2()
                .doOnNext(CountdownController::abortAtSix)
                .collectList().block()
        )).onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @GetMapping(path = "listerrorresponse", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CountData> getNoStreamErrorResponse2() {
        return streamSource2()
                .doOnNext(CountdownController::abortAtSix)
                .collectList()
                .block();
    }

    private Flux<CountData> streamSource2() {
        return Flux.just(10,9,8,7,6,5,4,3,2,1,0)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> new CountData(i, null))
                .doOnNext( element -> {
                    switch (element.getData()) {
                        case 5 -> element.setDesc("ignition");
                        case 0 -> element.setDesc("lift off");
                        default -> element.setDesc( String.valueOf(element.getData()) );
                    }
                });
    }

    private static void abortAtSix(CountData data) {
        if (data.getData() == 6) {
            throw new RuntimeException("abort");
        }
    }
}
