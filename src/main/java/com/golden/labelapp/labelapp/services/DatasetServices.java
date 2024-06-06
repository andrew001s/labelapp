package com.golden.labelapp.labelapp.services;

import java.util.List;

public interface DatasetServices {
    List<String> getFolder(String path);
    void generateDataset(List<String> name);
    StringBuilder convertJsonToYoloV5(String name);
} 