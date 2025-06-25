package com.soprasteria.css.reactivedemo.filter;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class BadNameFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String nameParameter = request.getQueryParams().getFirst("myname");
        if (null != nameParameter && nameParameter.contains("bad")) {
            request.getAttributes().put("myname", "World");
            request.getAttributes().put("badname", true);
        }

        return chain.filter(exchange);
    }
}
