package com.golden.labelapp.labelapp.services;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.golden.labelapp.labelapp.dto.Image;


public interface ImageServices {
    Map<String, Object> extractInfoFromJson(Image img);
    Image insertImage(Image img);
    List<Image> getAllImages();
    Image getImageById(int id);
    void deleteImage(int id);
    void convertToYoloV5(Map<String, Object> info_dict,List<Integer> id, int height, int width, String name);
    Map<String, Object> uploadImage(List<MultipartFile> file, String path);
    
}
