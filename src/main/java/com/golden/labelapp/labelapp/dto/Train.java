package com.golden.labelapp.labelapp.dto;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document   
public class Train extends Dataset{

    public Train(String name, List<ObjectDetect> objectdetect) {
        super(name, objectdetect);
    }

}
