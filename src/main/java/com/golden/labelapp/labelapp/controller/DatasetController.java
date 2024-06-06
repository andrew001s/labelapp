package com.golden.labelapp.labelapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golden.labelapp.labelapp.services.DatasetServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/dataset")
public class DatasetController {

    @Autowired
    private DatasetServices datasetServices;

    @Transactional
    @PostMapping("path")
    public ResponseEntity<?> getFiles(@RequestBody Map<String, String> path) {
        try
        {
            String pathString = path.get("path");
            List<String> names=datasetServices.getFolder(pathString);
            generateDataset(names);
            return ResponseEntity.ok(names);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    public void generateDataset(List<String> names) {
        List<String> name = new ArrayList<>();
        for (String n : names) {
            
            name.add(n);
        }
        datasetServices.generateDataset(name);

    }
    
    
    
}
