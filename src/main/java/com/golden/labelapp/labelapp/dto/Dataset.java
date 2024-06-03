package com.golden.labelapp.labelapp.dto;

import jakarta.persistence.Id;

public class Dataset {
    @Id
    private String id;
    private Object[] points;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Object[] getPoints() {
        return points;
    }
    public void setPoints(Object[] points) {
        this.points = points;
    }
    public Dataset(String id, Object[] points) {
        this.id = id;
        this.points = points;
    }
    
    
}
