package com.golden.labelapp.labelapp.models.entities;


import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
@Document(collection = "labels")
public class Labels {
    @Id
    private int id;
    @Indexed(unique = true)
    private String label;
    private int cant;
    private boolean isLogo;
    private String categoria;
    private String subcategoria;
    public Labels(int id, String label, int cant, boolean isLogo, String categoria, String subcategoria) {
        this.id = id;
        this.label = label;
        this.cant = cant;
        this.isLogo = isLogo;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
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

    public boolean isLogo() {
        return isLogo;
    }


    public void setLogo(boolean isLogo) {
        this.isLogo = isLogo;
    }


    public String getSubcategoria() {
        return subcategoria;
    }


    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }


    public String getCategoria() {
        return categoria;
    }


    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
}
