package com.golden.labelapp.labelapp.services;

import java.util.List;

import com.golden.labelapp.labelapp.dto.DatasetRequest;

public interface DatasetServices {
    List<String> getFolder(String path);
    void generateDataset(List<String> name);
    List<DatasetRequest> convertJsonToYoloV5(String name);
} 