package com.golden.labelapp.labelapp.services;

import java.util.List;

import com.golden.labelapp.labelapp.dto.Labels;

public interface LabelServices {
    List<Labels> getAllLabels();
    void saveLabel(String labelclass);
    
} 
