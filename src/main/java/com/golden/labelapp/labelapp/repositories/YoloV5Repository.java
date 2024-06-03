package com.golden.labelapp.labelapp.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.golden.labelapp.labelapp.dto.ObjectDetect;
import com.golden.labelapp.labelapp.dto.YoloV5;

public interface YoloV5Repository  extends MongoRepository<YoloV5, String>{

    void save(List<ObjectDetect> yoloV5);

    
} 
