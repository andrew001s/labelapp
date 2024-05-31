package com.golden.labelapp.labelapp.dto;

import java.util.List;


public class ObjectDetect {


    private int idlabel;
    private List<Object> points;
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
    public ObjectDetect(int idlabel, List<Object> points) {
        this.idlabel = idlabel;
        this.points = points;
    }
 

    
   
}
