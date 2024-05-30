package com.golden.labelapp.labelapp.dto;

public class Shapes {
    private String label;
    private Object[] points;
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public Object[] getPoints() {
        return points;
    }
    public void setPoints(Object[] points) {
        this.points = points;
    }
    public Shapes(String label, Object[] points) {
        this.label = label;
        this.points = points;
    }
    
}
