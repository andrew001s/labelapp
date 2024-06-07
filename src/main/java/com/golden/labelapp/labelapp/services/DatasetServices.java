package com.golden.labelapp.labelapp.services;

import java.util.List;
import java.util.Map;

import com.golden.labelapp.labelapp.dto.DatasetRequest;


public interface DatasetServices {
    List<String> getFolder(String path);
    void generateDataset(List<String> name);
    List<DatasetRequest> convertJsonToYoloV5(String name);
    Map<String, Object> generate_config_yaml(List<String> names, List<String>keys);
} 