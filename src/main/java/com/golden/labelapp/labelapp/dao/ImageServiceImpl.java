package com.golden.labelapp.labelapp.dao;

import com.golden.labelapp.labelapp.dto.Image;
import com.golden.labelapp.labelapp.services.ImageServices;


import java.util.Map;
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

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> extractInfoFromJson(Image img) {
        int index = 0;
        Map<String, Object> info_dict = new HashMap<>();
        Map<String, Object> bboxes = new HashMap<>();
        info_dict.put("filename", img.getName());
        info_dict.put("bboxes", bboxes);
        for (Object element : img.getShapes()) {
            Map<String, Object> bbox = new HashMap<>();
            bboxes.put("bbox" + index, bbox);
            String label = (String) ((Map<String, Object>) element).get("label");
            labelServicesImpl.saveLabel(label);
            bbox.put("label", label);
            bbox.put("points", ((Map<String, Object>) element).get("points"));
            index++;
            
        }
        
        info_dict.put("size", img.getSize());
        saveImage(img);
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

}
