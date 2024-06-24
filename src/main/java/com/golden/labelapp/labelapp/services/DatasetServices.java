package com.golden.labelapp.labelapp.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.golden.labelapp.labelapp.models.dtos.DatasetRequestDto;

/**
 * Esta interfaz define los servicios relacionados con los conjuntos de datos.
 */
public interface DatasetServices {
    
    /**
     * Obtiene la lista de carpetas en la ruta especificada.
     * 
     * @param path la ruta de la carpeta
     * @return la lista de nombres de las carpetas
     */
    List<String> getFolder(String path);
    
    /**
     * Genera un conjunto de datos utilizando los nombres especificados.
     * 
     * @param name los nombres a utilizar
     */
    void generateDataset(List<String> name);
    
    /**
     * Convierte un archivo JSON a formato YOLOv5.
     * 
     * @param name el nombre del archivo JSON
     * @return la lista de objetos DatasetRequest en formato YOLOv5
     */
    //List<DatasetRequestDto> convertJsonToYoloV5(String name);
    
    /**
     * Genera un archivo de configuración YAML utilizando los nombres y claves especificados.
     * 
     * @param names los nombres a utilizar
     * @param keys las claves a utilizar
     * @return un mapa que contiene la configuración YAML generada
     */
    Map<String, Object> generate_config_yaml(Map<Integer, String> keys);

    /**
     * Obtiene una lista de nombres de archivos en una carpeta dada.
     * 
     * @param page El número de página.
     * @param size El tamaño de la página.
     * @return Pagina de DatasetRequestDto que contiene las imagenes y sus bboxes 
     * @throws RuntimeException Si la ruta de la carpeta no es válida o no existe.
     */
    Page<DatasetRequestDto> getGraph(int page, int size);
}
