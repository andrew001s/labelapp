package com.golden.labelapp.labelapp.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.golden.labelapp.labelapp.dto.Image;
import com.golden.labelapp.labelapp.dto.Labels;


public interface ImageServices {
    Map<String, Object> extractInfoFromJson(Image img);
    Image insertImage(Image img);
    List<Image> getAllImages();
    Optional<Image> updateImage(Image img, int id);
    Optional<Image> getImageById(int id);
    void deleteImage(int id);
    void convertToYoloV5(Map<String, Object> info_dict, int height, int width, String name,List<Labels> labels);
    Map<String, Object> uploadImage(List<MultipartFile> file, String path);
    
}
