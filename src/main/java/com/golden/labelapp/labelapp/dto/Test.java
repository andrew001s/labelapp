package com.golden.labelapp.labelapp.dto;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "test")
public class Test extends Dataset{

    public Test(String name, List<ObjectDetect> objectdetect) {
        super(name, objectdetect);
    }

}
