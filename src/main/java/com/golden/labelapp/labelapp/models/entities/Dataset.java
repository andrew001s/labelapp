package com.golden.labelapp.labelapp.models.entities;

import java.util.List;

import com.golden.labelapp.labelapp.models.dtos.ObjectDetectDto;

import jakarta.persistence.Id;

public class Dataset {
    @Id
    private String id;
    private String name;
    private List<ObjectDetectDto> objectdetect;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<ObjectDetectDto> getPoints() {
        return objectdetect;
    }
    public void setPoints(List<ObjectDetectDto> objectdetect) {
        this.objectdetect = objectdetect;
    }
    public Dataset(String name, List<ObjectDetectDto> objectdetect) {
        this.name = name;
        this.objectdetect = (List<ObjectDetectDto>) objectdetect;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    } 

    
    
}
