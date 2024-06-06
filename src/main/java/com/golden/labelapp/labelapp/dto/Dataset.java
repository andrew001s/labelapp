package com.golden.labelapp.labelapp.dto;

import java.util.List;

import jakarta.persistence.Id;

public class Dataset {
    @Id
    private String id;
    private String name;
    private List<ObjectDetect> objectdetect;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<ObjectDetect> getPoints() {
        return objectdetect;
    }
    public void setPoints(List<ObjectDetect> objectdetect) {
        this.objectdetect = objectdetect;
    }
    public Dataset(String name, List<ObjectDetect> objectdetect) {
        this.name = name;
        this.objectdetect = (List<ObjectDetect>) objectdetect;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    } 

    
    
}
