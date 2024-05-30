package com.golden.labelapp.labelapp.services;

import java.util.List;
import java.util.Map;



import com.golden.labelapp.labelapp.dto.Image;

public interface ImageServices {
    Map<String, Object> extractInfoFromJson(Image img);
    Image insertImage(Image img);
    List<Image> getAllImages();
    Image getImageById(int id);
    void deleteImage(int id);
}
