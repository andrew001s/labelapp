package com.golden.labelapp.labelapp.controller;

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
            return ResponseEntity.ok(datasetServices.getFolder(pathString));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    
}
