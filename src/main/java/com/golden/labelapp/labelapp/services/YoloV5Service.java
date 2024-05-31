package com.golden.labelapp.labelapp.services;

import java.util.List;

import com.golden.labelapp.labelapp.dto.ObjectDetect;
import com.golden.labelapp.labelapp.dto.YoloV5;

public interface YoloV5Service {
    void saveYoloV5(String name,List<ObjectDetect> yoloV5);
    List<YoloV5> getAllYoloV5();
}
