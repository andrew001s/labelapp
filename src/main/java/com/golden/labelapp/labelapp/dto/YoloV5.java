package com.golden.labelapp.labelapp.dto;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document(collection = "yolov5")
public class YoloV5 {
    @Id
    private String id;
    private String name;
    private List<ObjectDetect> objectdetect;

    public Object getObjectdetect() {
        return objectdetect;
    }

    public void setObjectdetect(List<ObjectDetect> objectdetect) {
        this.objectdetect = objectdetect;
    }

    public YoloV5(String name,List<ObjectDetect> objectdetect) {
        this.name = name;
        this.objectdetect = objectdetect;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

    

}
