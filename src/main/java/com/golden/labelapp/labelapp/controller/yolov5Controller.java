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
     * @param num_labels el número mínimo de etiquetas que debe tener una imagen (Modificado para cambiar el numero requerido de etiquetas)
     * @return una respuesta HTTP indicando el resultado de la conversión
     */
    @PostMapping("/ToYoloV5")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> ToYoloV5(@RequestParam(defaultValue = "13") int num_labels){
        try {
            // Se crea un diccionario para almacenar la información de las imágenes
            Map<String, Object> info_dict = new HashMap<>();
            // Se crea un diccionario para almacenar las etiquetas y sus IDs
            Map<String, Integer> labelToId = new HashMap<>();
            List<Labels> labels = new ArrayList<>();
            List<Image> images = imageServices.getAllImages();
            int id = 0;
            // Se recorren las imágenes
            for (Image img : images) {
                // Se extrae la información de la imagen
                info_dict = imageServices.extractInfoFromJson(img);
                // Se recorren los elementos de la imagen
                for (Object element : img.getShapes()) {
                    // Se extrae la etiqueta del elemento
                    String label = (String) ((Map<String, Object>) element).get("label");
                    // Se verifica si la etiqueta ya ha sido añadida
                    if (!labels.stream().map(Labels::getLabel).collect(Collectors.toList()).contains(label)) {
                        int cant =0;
                        // Se verifica si la etiqueta ya existe en la base de datos
                        if (labelServicesImpl.getLabelByName(label) != null) {
                           cant = labelServicesImpl.getLabelByName(label).getCant();
                        } else {
                            
                            cant= labelServicesImpl.getLabelSubcategoria(label).getCant();
                        }
                        // Se verifica si la cantidad de etiquetas es mayor o igual al número minimo de etiquetas solicitadas
                        if (cant >= num_labels) {
                            Labels labelObj = new Labels(id, label, cant, false, " ", " ");
                            labels.add(labelObj);
                            id++;
                        }
                    }
                }
                // Se convierte la imagen al formato YOLOv5
                imageServices.convertToYoloV5(info_dict, img.getHeight(), img.getWidth(), img.getName(), labels,num_labels);
                labelToId.clear();
            }
            return ResponseEntity.ok().body("Convertido a YoloV5");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al convertir a YoloV5" + e.getMessage());
        }
    }
}
