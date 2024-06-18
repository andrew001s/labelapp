package com.golden.labelapp.labelapp.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.golden.labelapp.labelapp.models.dtos.ObjectDetectDto;
import com.golden.labelapp.labelapp.models.entities.YoloV5;

public interface YoloV5Repository  extends MongoRepository<YoloV5, String>{

    void save(List<ObjectDetectDto> yoloV5);
    YoloV5 findByName(String name);


} 
