package com.golden.labelapp.labelapp.models.entities;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.golden.labelapp.labelapp.models.dtos.ObjectDetectDto;

@Document   
public class Train extends Dataset{

    public Train(String name, List<ObjectDetectDto> objectdetect) {
        super(name, objectdetect);
    }

}
