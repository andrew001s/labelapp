package com.golden.labelapp.labelapp.dto;

import java.util.List;

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

    public String toStringLabe(List<Labels> labels) {
        String result = "";
        for (Labels label : labels) {
            result=result+"\n"+ "- " + label.getLabel();
        }
        return result;
    }

    @Override
public String toString() {
    return "Labels{" +
            "id=" + id +
            ", nombre='" + label + '\'' +
            ", logo=" + isLogo +
            ", cant=" + cant +
            ", subcategoria='" + subcategoria + '\'' +
            ", categoria='" + categoria + '\'' +
            '}';
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
