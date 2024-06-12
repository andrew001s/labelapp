package com.golden.labelapp.labelapp.dto;

import java.util.List;


public class ObjectDetect {


    private int idlabel;
    private List<Object> points;
    private String label;
    public int getIdlabel() {
        return idlabel;
    }
    public void setIdlabel(int idlabel) {
        this.idlabel = idlabel;
    }
    public List<Object> getPoints() {
        return points;
    }
    public void setPoints(List<Object> points) {
        this.points = points;
    }
    public ObjectDetect(int idlabel, List<Object> points, String label) {
        this.label = label;
        this.idlabel = idlabel;
        this.points = points;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
 

    
   
}
