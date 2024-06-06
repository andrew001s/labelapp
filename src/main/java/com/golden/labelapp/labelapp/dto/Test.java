package com.golden.labelapp.labelapp.dto;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "test")
public class Test extends Dataset{

    public Test(String id, List<ObjectDetect> points) {
        super(id, points);
    }

}
