package com.golden.labelapp.labelapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.golden.labelapp.labelapp.dto.YoloV5;
import com.golden.labelapp.labelapp.services.DatasetServices;
import com.golden.labelapp.labelapp.services.YoloV5Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/yolov5")
public class yolov5Controller {

    @Autowired
    private YoloV5Service yoloV5Impl;

    @Autowired
    private DatasetServices datasetServices;

    @Transactional(readOnly = true)
    @GetMapping("/all")
    public List<YoloV5> getAllYolov5() {
        return (List<YoloV5>) yoloV5Impl.getAllYoloV5();
    }

    @Transactional(readOnly = true)
    @GetMapping("/get/{name}")
    public ResponseEntity<String> getYolov5ByName(@PathVariable String name) {
        StringBuilder result = datasetServices.convertJsonToYoloV5(name);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        String filename = name.replaceAll("\\.(jpeg|png|jpg)$", ".txt");
        headers.setContentDispositionFormData("attachment", filename);
        return ResponseEntity.ok().headers(headers).body(result.toString());
    }

   
    
}
