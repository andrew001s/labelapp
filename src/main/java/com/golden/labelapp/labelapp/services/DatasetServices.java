package com.golden.labelapp.labelapp.services;

import java.util.List;
import java.util.Map;

import com.golden.labelapp.labelapp.dto.DatasetRequest;

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
    List<DatasetRequest> convertJsonToYoloV5(String name);
    
    /**
     * Genera un archivo de configuración YAML utilizando los nombres y claves especificados.
     * 
     * @param names los nombres a utilizar
     * @param keys las claves a utilizar
     * @return un mapa que contiene la configuración YAML generada
     */
    Map<String, Object> generate_config_yaml(List<String> names, Map<Integer, String> keys);
}
