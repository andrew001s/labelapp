package com.golden.labelapp.labelapp.dto;

import java.util.List;

import jakarta.persistence.Id;

public class Dataset {
    @Id
    private String id;
    private List<ObjectDetect> points;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<ObjectDetect> getPoints() {
        return points;
    }
    public void setPoints(List<ObjectDetect> points) {
        this.points = points;
    }
    public Dataset(String id, List<ObjectDetect> points) {
        this.id = id;
        this.points = (List<ObjectDetect>) points;
    } 

    
    
}
