package com.golden.labelapp.labelapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.golden.labelapp.labelapp.models.entities.Image;
import com.golden.labelapp.labelapp.models.entities.Labels;
import com.golden.labelapp.labelapp.models.entities.YoloV5;
import com.golden.labelapp.labelapp.services.ImageServices;
import com.golden.labelapp.labelapp.services.LabelServices;
import com.golden.labelapp.labelapp.services.YoloV5Service;

/**
 * Controlador para la funcionalidad relacionada con YOLOv5.
 */
@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/yolov5")
public class yolov5Controller {
    
    @Autowired
    private YoloV5Service yoloV5Impl;


    @Autowired
    private LabelServices labelServicesImpl;

    @Autowired
    private ImageServices imageServices;

    /**
     * Obtiene todos los objetos YOLOv5.
     * 
     * @return una lista de objetos YOLOv5
     */
    @GetMapping("/all")
    public Page<YoloV5> getAllYolov5(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return yoloV5Impl.getAllYoloV5(page, size);
    }

    /**
     * Convierte las imágenes y sus etiquetas al formato YOLOv5.
     * 
     * @return una respuesta HTTP indicando el resultado de la conversión
     */
    @PostMapping("/ToYoloV5")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> ToYoloV5(@RequestParam(defaultValue = "13") int num_labels){
        try {
            Map<String, Object> info_dict = new HashMap<>();
            Map<String, Integer> labelToId = new HashMap<>();
            List<Labels> labels = new ArrayList<>();
            List<Image> images = imageServices.getAllImages();
            int id = 0;
            for (Image img : images) {
                info_dict = imageServices.extractInfoFromJson(img);
                for (Object element : img.getShapes()) {
                    String label = (String) ((Map<String, Object>) element).get("label");

                    if (!labels.stream().map(Labels::getLabel).collect(Collectors.toList()).contains(label)) {
                        int cant =0;
                        if (labelServicesImpl.getLabelByName(label) != null) {
                           cant = labelServicesImpl.getLabelByName(label).getCant();
                        } else {
                            
                            cant= labelServicesImpl.getLabelSubcategoria(label).getCant();
                        }
                        if (cant >= num_labels) {
                            Labels labelObj = new Labels(id, label, cant, false, " ", " ");
                            labels.add(labelObj);
                            id++;
                        }
                    }
                }
                imageServices.convertToYoloV5(info_dict, img.getHeight(), img.getWidth(), img.getName(), labels,num_labels);
                labelToId.clear();
            }
            return ResponseEntity.ok().body("Convertido a YoloV5");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al convertir a YoloV5" + e.getMessage());
        }
    }
}
