package com.golden.labelapp.labelapp.dto;

import org.springframework.data.mongodb.core.mapping.Document;

@Document   
public class Train extends Dataset{

    public Train(String id, Object[] points) {
        super(id, points);
    }

}
