package com.golden.labelapp.labelapp.dto;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "test")
public class Test extends Dataset{

    public Test(String id, Object[] points) {
        super(id, points);
    }

}
