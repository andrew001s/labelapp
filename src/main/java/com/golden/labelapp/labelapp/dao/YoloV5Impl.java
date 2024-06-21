package com.golden.labelapp.labelapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.golden.labelapp.labelapp.models.dtos.ObjectDetectDto;
import com.golden.labelapp.labelapp.models.entities.YoloV5;
import com.golden.labelapp.labelapp.repositories.YoloV5Repository;
import com.golden.labelapp.labelapp.services.YoloV5Service;

/**
 * Implementación de la interfaz YoloV5Service que proporciona métodos para guardar y recuperar objetos YoloV5.
 */
@Service
public class YoloV5Impl implements YoloV5Service{

    @Autowired
    private YoloV5Repository yoloV5Repository;
    
    /**
     * Guarda un objeto YoloV5 en la base de datos.
     * 
     * @param name el nombre del objeto YoloV5
     * @param obj la lista de objetos detectados
     */
    @Transactional
    @Override
    public void saveYoloV5(String name, String ruta, List<ObjectDetectDto> obj) {
        YoloV5 yoloV5 = new YoloV5(name,ruta,obj);
        yoloV5Repository.save(yoloV5);
    }

    /**
     * Obtiene todos los objetos YoloV5 almacenados en la base de datos.
     * 
     * @return una lista de objetos YoloV5
     */
    @Transactional(readOnly = true)
    @Override
    public Page<YoloV5> getAllYoloV5(int page, int size) {
        return yoloV5Repository.findAll(PageRequest.of(page, size));
    }

    /**
     * Obtiene un objeto YoloV5 por su nombre.
     * 
     * @param name el nombre del objeto YoloV5 a buscar
     * @return el objeto YoloV5 encontrado, o null si no se encuentra ninguno con ese nombre
     */
    @Transactional(readOnly = true)
    @Override
    public YoloV5 getYoloV5ByName(String name) {
        return yoloV5Repository.findByName(name);
    }

    /**
     * Obtiene todos los objetos YoloV5 almacenados en la base de datos.
     * 
     * @return una lista de objetos YoloV5
     */
    @Transactional(readOnly = true)
    @Override
    public List<YoloV5> getAllYoloV5List() {
        return yoloV5Repository.findAll();
    }

}
