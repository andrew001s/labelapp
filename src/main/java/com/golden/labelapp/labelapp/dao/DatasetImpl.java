package com.golden.labelapp.labelapp.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golden.labelapp.labelapp.dto.ObjectDetect;
import com.golden.labelapp.labelapp.dto.Train;
import com.golden.labelapp.labelapp.dto.YoloV5;
import com.golden.labelapp.labelapp.repositories.TrainRepository;
import com.golden.labelapp.labelapp.repositories.YoloV5Repository;
import com.golden.labelapp.labelapp.services.DatasetServices;

@Service
public class DatasetImpl implements DatasetServices {

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private YoloV5Repository yoloV5Repository;

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
        List<String> traindataSet = new ArrayList<>();
        List<String> testdataSet = new ArrayList<>();
        List<String> validationdataSet = new ArrayList<>();
        int totalSize = name.size();
        int trainSize = (int) (totalSize * train);
        int validationSize = (int) (totalSize * validation);
        // int testSize=totalSize-trainSize-validationSize;
        trainRepository.deleteAll();
        Collections.shuffle(name);
        for (int i = 0; i < trainSize; i++) {
            YoloV5 yolofile = yoloV5Repository.findByName(name.get(i));
            if (yolofile != null) {
               
                List<ObjectDetect> objectdetect = yolofile.getObjectdetect();
                Train trainfile = new Train(name.get(i), objectdetect);
                trainRepository.save(trainfile);
            }

        }
        for (int i = trainSize; i < trainSize + validationSize; i++) {
            validationdataSet.add(name.get(i));
        }
        for (int i = trainSize + validationSize; i < totalSize; i++) {
            testdataSet.add(name.get(i));
        }

    }

    @Override
    public StringBuilder convertJsonToYoloV5(String name) {
        YoloV5 yoloV5 = yoloV5Repository.findByName(name);
        StringBuilder result = new StringBuilder();
        for (ObjectDetect od : yoloV5.getObjectdetect()) {
            result.append(od.getIdlabel()).append(" ");
            for (Object point : od.getPoints()) {
                String pointString = point.toString().replace("[", "")
                        .replace("]", "").replace(",", "");

                result.append(pointString).append(" ");
            }
            result.deleteCharAt(result.length() - 1);
            result.append("\n");
        }
        return result;
    }

}
