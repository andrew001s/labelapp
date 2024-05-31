package com.golden.labelapp.labelapp.dao;

import com.golden.labelapp.labelapp.dto.Image;
import com.golden.labelapp.labelapp.dto.ObjectDetect;
import com.golden.labelapp.labelapp.dto.YoloV5;
import com.golden.labelapp.labelapp.services.ImageServices;


import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.golden.labelapp.labelapp.repositories.ImageRespository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageServices {

    @Autowired
    private ImageRespository imageRepository;
    @Autowired
    private LabelServicesImpl labelServicesImpl;

    @Autowired
    private YoloV5Impl yoloV5Impl;

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> extractInfoFromJson(Image img) {
        int index = 0;
        Map<String, Object> info_dict = new HashMap<>();
        Map<String, Object> bboxes = new HashMap<>();
        info_dict.put("filename", img.getName());
        List<Integer> idlabel=new ArrayList<>();
        info_dict.put("bboxes", bboxes);
        for (Object element : img.getShapes()) {
            Map<String, Object> bbox = new HashMap<>();
            bboxes.put("bbox" + index, bbox);
            String label = (String) ((Map<String, Object>) element).get("label");
            labelServicesImpl.saveLabel(label);
            bbox.put("label", label);
            bbox.put("points", ((Map<String, Object>) element).get("points"));
            index++;
            idlabel.add(labelServicesImpl.getLabelId(label));
            
        }
        
        info_dict.put("size", img.getSize());
        saveImage(img);
        
        convertToYoloV5(info_dict,idlabel,img.getHeight(),img.getWidth(),img.getName());
        return info_dict;
    }

    

    public Image saveImage(Image img) {
        int id=0;
        if (imageRepository.findAll().size() > 0) {
            id = imageRepository.findAll().get(imageRepository.findAll().size() - 1).getId() + 1;
        }
        img.setId(id);
        return imageRepository.save(img);
    }
   

    @Override
    public Image insertImage(Image img) {

        return img;

    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public Image getImageById(int id) {
        return imageRepository.findById(id).get();
    }

    @Override
    public void deleteImage(int id) {
        imageRepository.deleteById(id);
    }


    @SuppressWarnings("unchecked")
    @Override
    public void convertToYoloV5(Map<String, Object> info_dict,List<Integer> id, int height, int width, String name) {
        
        Map<String, Object> bboxes = (Map<String, Object>) info_dict.get("bboxes");
        List<ObjectDetect> objlist = new ArrayList<>();

        int i=0;
        for (String key : bboxes.keySet()) {
            Map<String, Object> bbox = (Map<String, Object>) bboxes.get(key);
            List<List<Double>> points = (List<List<Double>>) bbox.get("points");
            List<Object> retval = new ArrayList<>();
            for (List<Double> point : points) {
                double x = Math.round((double) point.get(0) / width * 100000.0) / 100000.0;
                double y = Math.round((double) point.get(1) / height * 100000.0) / 100000.0;
                List<Double> pointList = new ArrayList<>();
                pointList.add(x);
                pointList.add(y);
                retval.add(pointList);
            }
            int idlabel=id.get(i);
            ObjectDetect yolo = new ObjectDetect(idlabel, retval);
            objlist.add(yolo);
            i++;
           
        }
        yoloV5Impl.saveYoloV5(name,objlist);
       
    }

}
