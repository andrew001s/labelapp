package com.golden.labelapp.labelapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golden.labelapp.labelapp.dto.ObjectDetect;
import com.golden.labelapp.labelapp.dto.YoloV5;
import com.golden.labelapp.labelapp.repositories.YoloV5Repository;
import com.golden.labelapp.labelapp.services.YoloV5Service;

@Service
public class YoloV5Impl implements YoloV5Service{


    @Autowired
    private YoloV5Repository yoloV5Repository;
    
    @Override
    public void saveYoloV5(String name, List<ObjectDetect> obj) {
        YoloV5 yoloV5 = new YoloV5(name,obj);
        yoloV5Repository.save(yoloV5);
    }

    @Override
    public List<YoloV5> getAllYoloV5() {
        return yoloV5Repository.findAll();
    }

}
