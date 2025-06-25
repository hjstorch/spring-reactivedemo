package com.soprasteria.css.reactivedemo.quote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Quote {

    private String content;
    private String author;
}
