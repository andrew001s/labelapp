package com.golden.labelapp.labelapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golden.labelapp.labelapp.dao.YoloV5Impl;
import com.golden.labelapp.labelapp.dto.YoloV5;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/yolov5")
public class yolov5Controller {

    @Autowired
    private YoloV5Impl yoloV5Impl;

    @Transactional(readOnly = true)
    @GetMapping("/all")
    public List<YoloV5> getAllYolov5() {
        return (List<YoloV5>) yoloV5Impl.getAllYoloV5();
    }
    
}
