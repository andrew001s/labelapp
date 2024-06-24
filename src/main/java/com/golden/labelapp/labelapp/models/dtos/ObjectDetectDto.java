package com.golden.labelapp.labelapp.models.dtos;

import java.util.List;

/**
 * Clase que representa un objeto detectado.
 */
public class ObjectDetectDto {


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
    /**
     * Constructor de la clase ObjectDetectDto.
     * 
     * @param idlabel el ID de la etiqueta
     * @param points los puntos del objeto detectado
     * @param label la etiqueta del objeto detectado
     */
    public ObjectDetectDto(int idlabel, List<Object> points, String label) {
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
