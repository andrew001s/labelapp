package com.golden.labelapp.labelapp.dto;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "images")
public class Image {

    private int id;
    private String name;
    private Object[] shapes;
    private int width;
    private int height;
    
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
}
