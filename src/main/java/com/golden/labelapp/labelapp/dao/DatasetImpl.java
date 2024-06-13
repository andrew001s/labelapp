package com.golden.labelapp.labelapp.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golden.labelapp.labelapp.dto.DatasetRequest;
import com.golden.labelapp.labelapp.dto.ObjectDetect;
import com.golden.labelapp.labelapp.dto.Test;
import com.golden.labelapp.labelapp.dto.Train;
import com.golden.labelapp.labelapp.dto.Validation;
import com.golden.labelapp.labelapp.dto.YoloV5;
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

    private boolean isImage(String path) {
        String[] validExtensions = { "jpg", "jpeg", "png" };
        for (String extension : validExtensions) {
            if (path.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void generateDataset(List<String> name) {
        double train = 0.6;
        // double test=0.2;
        double validation = 0.2;

        int totalSize = name.size();
        int trainSize = (int) (totalSize * train);
        int validationSize = (int) (totalSize * validation);
        // int testSize=totalSize-trainSize-validationSize;
        trainRepository.deleteAll();
        validationRepository.deleteAll();
        testRepository.deleteAll();
        Collections.shuffle(name);

        for (int i = 0; i < trainSize; i++) {
            YoloV5 yolofiletrain = yoloV5Repository.findByName(name.get(i));
            if (yolofiletrain != null) {
                List<ObjectDetect> objectdetect = yolofiletrain.getObjectdetect();
                
                Train trainfile = new Train(name.get(i), objectdetect);
                trainRepository.save(trainfile);
                
            }
        }
        for (int i = trainSize; i < trainSize + validationSize; i++) {
            YoloV5 yolofilevalidation = yoloV5Repository.findByName(name.get(i));
            if (yolofilevalidation != null) {
                List<ObjectDetect> objectdetect = yolofilevalidation.getObjectdetect();
                
                Validation validationfile = new Validation(name.get(i), objectdetect);
                validationRepository.save(validationfile);
                
            }
        }
        for (int i = trainSize + validationSize; i < totalSize; i++) {
            YoloV5 yolofiletest = yoloV5Repository.findByName(name.get(i));
            if (yolofiletest != null) {
                List<ObjectDetect> objectdetect = yolofiletest.getObjectdetect();
              
                
                Test testfile = new Test(name.get(i), objectdetect);
                testRepository.save(testfile);
                
            }
        }

    }

  
    @Override
    public List<DatasetRequest> convertJsonToYoloV5(String collectionName) {
        List<?> documents;

        switch (collectionName.toLowerCase()) {
            case "train":
                documents = trainRepository.findAll();
                break;
            case "test":
                documents = testRepository.findAll();
                break;
            case "validation":
                documents = validationRepository.findAll();
                break;
            default:
                throw new IllegalArgumentException("Invalid collection name: " + collectionName);
        }

        List<DatasetRequest> resultList = new ArrayList<>();
        
        for (Object doc : documents) {
            StringBuilder result = new StringBuilder();
            String docName;
            List<ObjectDetect> objectDetects;
            
            if (doc instanceof Train) {
                docName = ((Train) doc).getName();
                objectDetects = ((Train) doc).getPoints();
            } else if (doc instanceof Test) {
                docName = ((Test) doc).getName();
                objectDetects = ((Test) doc).getPoints();
            } else if (doc instanceof Validation) {
                docName = ((Validation) doc).getName();
                objectDetects = ((Validation) doc).getPoints();
            } else {
                continue;
            }

            for (ObjectDetect od : objectDetects) {
                result.append(od.getIdlabel()).append(" ");
                for (Object point : od.getPoints()) {
                    String pointString = point.toString().replace("[", "")
                            .replace("]", "").replace(",", "");
                    result.append(pointString).append(" ");
                }
                result.deleteCharAt(result.length() - 1);
                result.append("\n");
            }
            resultList.add(new DatasetRequest(docName, result));
        }
        
        return resultList;
    }

    @Override
    public Map<String, Object> generate_config_yaml(List<String> names, Map<Integer, String> keys) {
        // Crear un LinkedHashMap para preservar el orden de inserción
        Map<String, Object> configYaml = new LinkedHashMap<>();
        configYaml.put("path", "../dataset/images");
        configYaml.put("train", "train");
        configYaml.put("val", "val");
        configYaml.put("test", "test");
        configYaml.put("nc", keys.size());
        configYaml.put("names", keys);
        return configYaml;
    }
        
}




