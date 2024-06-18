package com.golden.labelapp.labelapp.models.entities;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.golden.labelapp.labelapp.models.dtos.ObjectDetectDto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
@Document(collection = "yolov5")
public class YoloV5 {
    @Id
    private String id;
    private String name;
    private List<ObjectDetectDto> objectdetect;



    public YoloV5(String name,List<ObjectDetectDto> objectdetect) {
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

    public List<ObjectDetectDto> getObjectdetect() {
        return objectdetect;
    }

    public void setObjectdetect(List<ObjectDetectDto> objectdetect) {
        this.objectdetect = objectdetect;
    }
    

    

}
