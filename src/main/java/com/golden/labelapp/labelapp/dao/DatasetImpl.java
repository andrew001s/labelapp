/**
 * Esta clase implementa la interfaz DatasetServices y proporciona la implementación de los métodos definidos en la interfaz.
 * Se encarga de realizar operaciones relacionadas con la manipulación de conjuntos de datos.
 */
package com.golden.labelapp.labelapp.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.golden.labelapp.labelapp.models.dtos.DatasetRequestDto;
import com.golden.labelapp.labelapp.models.dtos.ObjectDetectDto;
import com.golden.labelapp.labelapp.models.entities.Test;
import com.golden.labelapp.labelapp.models.entities.Train;
import com.golden.labelapp.labelapp.models.entities.Validation;
import com.golden.labelapp.labelapp.models.entities.YoloV5;
import com.golden.labelapp.labelapp.repositories.TestRepository;
import com.golden.labelapp.labelapp.repositories.TrainRepository;
import com.golden.labelapp.labelapp.repositories.ValidationRepository;
import com.golden.labelapp.labelapp.repositories.YoloV5Repository;
import com.golden.labelapp.labelapp.services.DatasetServices;

@Service
public class DatasetImpl implements DatasetServices {

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private YoloV5Repository yoloV5Repository;

    @Autowired
    private ValidationRepository validationRepository;

    @Autowired
    private TestRepository testRepository;

    /**
     * Obtiene una lista de nombres de archivos en una carpeta dada.
     * 
     * @param path La ruta de la carpeta.
     * @return Una lista de nombres de archivos en la carpeta.
     * @throws RuntimeException Si la ruta de la carpeta no es válida o no existe.
     */
    @Transactional(readOnly = true)
    @Override
    public List<String> getFolder(String path) {
        File folder = new File(path);
        List<String> files = new ArrayList<>();
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (isImage(file.getAbsolutePath()))
                    files.add(file.getName());
            }
        } else {
            throw new RuntimeException("La ruta de la carpeta no es válida o no existe");
        }

        return files;
    }

    /**
     * Verifica si un archivo dado es una imagen basándose en su extensión.
     * 
     * @param path La ruta del archivo.
     * @return true si el archivo es una imagen, false de lo contrario.
     */
    private boolean isImage(String path) {
        String[] validExtensions = { "jpg", "jpeg", "png" };
        for (String extension : validExtensions) {
            if (path.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Genera un conjunto de datos a partir de una lista de nombres de archivos.
     * Divide los archivos en conjuntos de entrenamiento, validación y prueba según
     * las proporciones especificadas.
     * 
     * @param name La lista de nombres de archivos.
     */
    @Transactional
    @Override
    public void generateDataset(List<String> name) {
        double train = 0.6;
        double validation = 0.2;

        int totalSize = name.size();
        int trainSize = (int) (totalSize * train);
        int validationSize = (int) (totalSize * validation);
        trainRepository.deleteAll();
        validationRepository.deleteAll();
        testRepository.deleteAll();
        Collections.shuffle(name);

        for (int i = 0; i < trainSize; i++) {
            YoloV5 yolofiletrain = yoloV5Repository.findByName(name.get(i));
            if (yolofiletrain != null) {
                List<ObjectDetectDto> objectdetect = yolofiletrain.getObjectdetect();

                Train trainfile = new Train(name.get(i), objectdetect);
                trainRepository.save(trainfile);

            }
        }
        for (int i = trainSize; i < trainSize + validationSize; i++) {
            YoloV5 yolofilevalidation = yoloV5Repository.findByName(name.get(i));
            if (yolofilevalidation != null) {
                List<ObjectDetectDto> objectdetect = yolofilevalidation.getObjectdetect();

                Validation validationfile = new Validation(name.get(i), objectdetect);
                validationRepository.save(validationfile);

            }
        }
        for (int i = trainSize + validationSize; i < totalSize; i++) {
            YoloV5 yolofiletest = yoloV5Repository.findByName(name.get(i));
            if (yolofiletest != null) {
                List<ObjectDetectDto> objectdetect = yolofiletest.getObjectdetect();

                Test testfile = new Test(name.get(i), objectdetect);
                testRepository.save(testfile);

            }
        }

    }

    /**
     * Convierte los documentos de una colección dada en objetos DatasetRequest.
     * 
     * @param page El número de página.
     * @param size El tamaño de la página.
     * @return Una lista de objetos DatasetRequest.
     * @throws IllegalArgumentException Si el nombre de la colección es inválido.
     */


    @Transactional(readOnly = true)
    @Override
    public Page<DatasetRequestDto> getGraph(int page, int size) {
        // Se obtienen los documentos de la colección
        List<?> documents = yoloV5Repository.findAll();
        List<DatasetRequestDto> resultList = new ArrayList<>();
        // Se recorren los documentos
        for (Object doc : documents) {
           
            String docName;
            String url;
            ArrayList<StringBuilder> resultsobject = new ArrayList<>(); 
            // Se extraen los datos de los documentos
            List<ObjectDetectDto> objectDetects;
            docName = ((YoloV5) doc).getName();
            objectDetects = ((YoloV5) doc).getObjectdetect();
            url = ((YoloV5) doc).getRuta();
            // Se recorren los objetos detectados
            for (ObjectDetectDto od : objectDetects) {
                StringBuilder result = new StringBuilder();
                result.append(od.getIdlabel()).append(" ");
                // Se recorren los puntos de los objetos detectados
                for (Object point : od.getPoints()) {
                    String pointString = point.toString().replace("[", "")
                            .replace("]", "").replace(",", "");
                    result.append(pointString);
                
                }
                resultsobject.add(result);
               
            }
            // Se añaden los resultados a la lista
            StringBuilder[] resultArray= resultsobject.toArray(new StringBuilder[resultsobject.size()]);
            resultList.add(new DatasetRequestDto(docName, url, resultArray));
            
        }
        // Se devuelve la lista paginada
        int start = Math.min((int) PageRequest.of(page, size).getOffset(), resultList.size());
        int end = Math.min((start + PageRequest.of(page, size).getPageSize()), resultList.size());
        return new PageImpl<>(resultList.subList(start, end), PageRequest.of(page, size), resultList.size());
    }

    /**
     * Genera un mapa de configuración YAML para YOLOv5 a partir de una lista de
     * nombres y un mapa de labels.
     * 
     * @param keys El mapa de labels.
     * @return Un mapa de configuración YAML.
     */
    @Override
    public Map<String, Object> generate_config_yaml(Map<Integer, String> keys) {

        Map<String, Object> configYaml = new LinkedHashMap<>();
        configYaml.put("path", "Agregar aquí la ruta Absoluta de la carpeta del dataset");
        configYaml.put("train", "train");
        configYaml.put("val", "validation");
        configYaml.put("test", "test");
        configYaml.put("nc", keys.size());
        configYaml.put("names", keys);
        return configYaml;

    }

}
