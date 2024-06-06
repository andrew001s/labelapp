package com.golden.labelapp.labelapp.dto;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Validation extends Dataset {

    public Validation(String id, List<ObjectDetect> points) {
        super(id, points);
    }

}
