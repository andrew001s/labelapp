package com.golden.labelapp.labelapp.models.entities;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

/**
 * Clase que representa una imagen.
 */
@Document(collection = "images")
public class Image {
    @Id
    private int id;
    @Indexed(unique = true)
    private String ruta;
    private int[] ids;
    @Indexed(unique = true)
    private String name;
    private Object[] shapes;
    private int width;
    private int height;
    private Date createdAt;
    private LocalDateTime updatedAt;
    
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public int[] getIds() {
        return ids;
    }
    public void setIds(int[] ids) {
        this.ids = ids;
    }

    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Object[] getShapes() {
        return shapes;
    }
    public void setShapes(Object[] shapes) {
        this.shapes = shapes;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int[] getSize() {
        return new int[] {width, height};
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getRuta() {
        return ruta;
    }
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

}
