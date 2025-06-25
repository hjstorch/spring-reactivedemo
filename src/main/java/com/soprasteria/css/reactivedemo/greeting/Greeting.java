package com.soprasteria.css.reactivedemo.greeting;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Greeting {

    private String greeting;
    private String quote;
}
