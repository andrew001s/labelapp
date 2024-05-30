package com.golden.labelapp.labelapp.dto;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document(collection = "labels")
public class Labels {
    @Id
    private int id;
    private String label;
    private int cant;

    public Labels(int id, String label, int cant) {
        this.id = id;
        this.label = label;
        this.cant = cant;
    }

   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
         this.cant = cant;
    }

}
