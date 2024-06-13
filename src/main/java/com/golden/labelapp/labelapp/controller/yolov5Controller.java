package com.golden.labelapp.labelapp.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.golden.labelapp.labelapp.dao.LabelServicesImpl;
import com.golden.labelapp.labelapp.dto.DatasetRequest;
import com.golden.labelapp.labelapp.dto.Image;
import com.golden.labelapp.labelapp.dto.Labels;
import com.golden.labelapp.labelapp.dto.ObjectDetect;
import com.golden.labelapp.labelapp.dto.YoloV5;
import com.golden.labelapp.labelapp.services.DatasetServices;
import com.golden.labelapp.labelapp.services.ImageServices;
import com.golden.labelapp.labelapp.services.YoloV5Service;

@RestController
@RequestMapping("/yolov5")
public class yolov5Controller {

    @Autowired
    private YoloV5Service yoloV5Impl;

    @Autowired
    private DatasetServices datasetServices;

    @Autowired
    private LabelServicesImpl labelServicesImpl;

    @Autowired
    private ImageServices imageServices;

    @Transactional(readOnly = true)
    @GetMapping("/all")
    public List<YoloV5> getAllYolov5() {
        return (List<YoloV5>) yoloV5Impl.getAllYoloV5();
    }

  

    @Transactional
    @PostMapping("/ToYoloV5")
    public ResponseEntity<?> ToYoloV5() {
        try {
            Map<String, Object> info_dict = new HashMap<>();
            Map<String, Integer> labelToId = new HashMap<>();
            List<Labels> labels = new ArrayList<>();
            List<Image> images = imageServices.getAllImages();
            int id = 0;
            for (Image img : images) {
                info_dict = imageServices.extractInfoFromJson(img);
                for (Object element : img.getShapes()) {
                    @SuppressWarnings("unchecked")
                    String label = (String) ((Map<String, Object>) element).get("label");
                    if (!labels.stream().map(Labels::getLabel).collect(Collectors.toList()).contains(label)) {
                        int cant = labelServicesImpl.getLabelByName(label).getCant();
                        if (cant>13){
                            Labels labelObj = new Labels(id, label,cant);
                            labels.add(labelObj);
                            id++; 
                        }
                                       
                    }
                }
                
                imageServices.convertToYoloV5(info_dict, img.getHeight(), img.getWidth(), img.getName(), labels);
                labelToId.clear();
            }
            return ResponseEntity.ok().body("Convertido a YoloV5");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al convertir a YoloV5"+e.getMessage());
        }
    }


}
