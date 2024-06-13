package com.golden.labelapp.labelapp.dao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.golden.labelapp.labelapp.dto.Image;
import com.golden.labelapp.labelapp.dto.Labels;
import com.golden.labelapp.labelapp.dto.ObjectDetect;
import com.golden.labelapp.labelapp.repositories.ImageRespository;
import com.golden.labelapp.labelapp.services.ImageServices;

 
@Service
public class ImageServiceImpl implements ImageServices {
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private ImageRespository imageRepository;


    @Autowired
    private YoloV5Impl yoloV5Impl;

    

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
            
            bbox.put("label", label);
            bbox.put("points", ((Map<String, Object>) element).get("points"));
            index++;
            
        }
        
        info_dict.put("size", img.getSize());
        //saveImage(img);
        
        
        return info_dict;
    }

    
    @Override
    public Image insertImage(Image img) {
        int id=0;
        if (imageRepository.findAll().size() > 0) {
            id = imageRepository.findAll().get(imageRepository.findAll().size() - 1).getId() + 1;
        }
        img.setId(id);
        return imageRepository.save(img);
    }
   
    @Override
    @SuppressWarnings("null")
    public Map<String, Object> uploadImage(List<MultipartFile> file, String path) {
        List<Map<String, Object>> responses = new ArrayList<>();
        for (MultipartFile f : file) {
            try {
                Path uploatdPath = Paths.get(path).toAbsolutePath().normalize();
                if(!Files.exists(uploatdPath)){
                    Files.createDirectories(uploatdPath);
                }
                String filename = f.getOriginalFilename();
                Path filepath = uploatdPath.resolve(filename);
                Files.write(filepath, f.getBytes());
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/")
                        .path(filename).toUriString();
                Map<String, Object> res = new HashMap<>();
                res.put("message", "File uploaded successfully");
                res.put("file", filename);
                res.put("url", fileDownloadUri);
                responses.add(res);
            } catch (Exception e) {
                Map<String, Object> res = new HashMap<>();
                res.put("message", "Could not upload the file: " + f.getOriginalFilename() + "!");
                responses.add(res);
                return res;
            }
        }
        return responses.get(0);
    
    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Image> getImageById(int id) {
        return Optional.ofNullable(imageRepository.findById(id).get());
    }

    @Override
    @Transactional
    public void deleteImage(int id) {
        imageRepository.deleteById(id);
    }
     
    @SuppressWarnings("unchecked")
    @Override
    public void convertToYoloV5(Map<String, Object> info_dict, int height, int width, String name,List<Labels> labels) {
            
        Map<String, Object> bboxes = (Map<String, Object>) info_dict.get("bboxes");
        List<ObjectDetect> objlist = new ArrayList<>();
      
        int i = 0;
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
        
            String labelName = (String) bbox.get("label");

            for (Labels label : labels) {
                if (label.getLabel().equals(labelName)) {
                    ObjectDetect obj = new ObjectDetect(label.getId(), retval, label.getLabel());
                    objlist.add(obj);
                    i++;
                }
            }
        }
            
           
        
        yoloV5Impl.saveYoloV5(name,objlist);
       
    }



    @Override
    @Transactional
    public Optional<Image> updateImage(Image img, int id) {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            Update update = new Update();
            update.set("name", img.getName());
            update.set("height", img.getHeight());
            update.set("width", img.getWidth());
            update.set("shapes", img.getShapes());
            
            Query query = Query.query(Criteria.where("_id").is(id));
            Image updatedImage = mongoTemplate.findAndModify(query, update, Image.class);
            
            // Verificar si se actualizó correctamente
            if (updatedImage != null) {
                return Optional.of(updatedImage);
            } else {
                // Aquí puedes decidir qué hacer si no se encuentra el documento para actualizar
                return Optional.empty();
            }
        } else {
            // Aquí puedes decidir qué hacer si no se encuentra el documento con el ID dado
            return Optional.empty();
        }
    }
    

}
