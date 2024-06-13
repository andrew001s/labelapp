package com.golden.labelapp.labelapp.services;

import java.util.List;
import java.util.Optional;

import com.golden.labelapp.labelapp.dto.Labels;

public interface LabelServices {
    List<Labels> getAllLabels();
    void saveLabel(String labelclass);
    int getLabelId(String labelclass);
    Labels getLabelById(int id);
    Labels getLabelByName(String name);
    void deleteLabel(int id);
    Optional<Labels> updateLabel(Labels label, int id);
    
} 
