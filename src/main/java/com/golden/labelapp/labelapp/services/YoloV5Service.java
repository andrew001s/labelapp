package com.golden.labelapp.labelapp.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.golden.labelapp.labelapp.models.dtos.ObjectDetectDto;
import com.golden.labelapp.labelapp.models.entities.YoloV5;

public interface YoloV5Service {
    
    /**
     * Saves the YoloV5 object with the given name and list of ObjectDetect.
     * 
     * @param name The name of the YoloV5 object.
     * @param yoloV5 The list of ObjectDetect to be saved.
     */
    void saveYoloV5(String name, String ruta,List<ObjectDetectDto> yoloV5);
    
    /**
     * Retrieves all YoloV5 objects.
     * @param page es numero de la pagina
     * @param size es el tamaño de la pagina
     * @return A list of all YoloV5 objects.
     */
    Page<YoloV5> getAllYoloV5(int page, int size);
    
    /**
     * Retrieves the YoloV5 object with the given name.
     * 
     * @param name The name of the YoloV5 object to retrieve.
     * @return The YoloV5 object with the given name, or null if not found.
     */
    YoloV5 getYoloV5ByName(String name);

    /**
     * Regresa todos los objetos YoloV5.
     * 
     * @return A list of all YoloV5 objects.
     */
    List<YoloV5> getAllYoloV5List();
}
