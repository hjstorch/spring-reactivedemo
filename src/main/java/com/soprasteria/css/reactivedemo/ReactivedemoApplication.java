package com.soprasteria.css.reactivedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactivedemoApplication {

    public static void main(String[] args) {
        // init reactor tools needed for debugging while development
        // ReactorDebugAgent.init(); // Intellij injects this for you
        SpringApplication.run(ReactivedemoApplication.class, args);
    }

}
