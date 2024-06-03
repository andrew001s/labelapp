package com.golden.labelapp.labelapp.dto;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Validation extends Dataset {

    public Validation(String id, Object[] points) {
        super(id, points);
    }

}
