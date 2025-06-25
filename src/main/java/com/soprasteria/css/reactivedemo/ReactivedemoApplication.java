package com.soprasteria.css.reactivedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
public class ReactivedemoApplication {

    public static void main(String[] args) {
        // init reactor tools needed for debugging while development
        // ReactorDebugAgent.init(); // Intellij injects this for you
        SpringApplication.run(ReactivedemoApplication.class, args);
    }

}
