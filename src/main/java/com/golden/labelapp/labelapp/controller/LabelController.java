package com.golden.labelapp.labelapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golden.labelapp.labelapp.dto.Image;
import com.golden.labelapp.labelapp.dto.Labels;
import com.golden.labelapp.labelapp.services.ImageServices;
import com.golden.labelapp.labelapp.services.LabelServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;







@RestController
@RequestMapping("/label")
public class LabelController {
    @Autowired
    private LabelServices labelServicesImpl;
    @Autowired
    private ImageServices imageServices;
    @Transactional(readOnly = true)
    @GetMapping("/all")
    public List<Labels> getAllLabels() {
        return (List<Labels>) labelServicesImpl.getAllLabels();
    }

    @Transactional(readOnly = true)
    @GetMapping("/getid/{name}")
    public int getLabelByName(@PathVariable String name) {
        return labelServicesImpl.getLabelId(name);
    }

    @Transactional
    @PostMapping("/saveLabel")
    public String saveLabel() {
        
        List<Image> images = imageServices.getAllImages();
        for (Object element : images) {
            for (Object element2 : ((Image) element).getShapes()) {
                @SuppressWarnings("unchecked")
                String label = (String) ((Map<String, Object>) element2).get("label");
                labelServicesImpl.saveLabel(label);
            }

        }
        return "Finalizado";
    }
    
    @Transactional
    @PostMapping("/insertLabel")
    public String insertLabel(@RequestBody String label) {
        try {
            labelServicesImpl.saveLabel(label);
            return "Label inserted";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PutMapping("update/{id}")
    @Transactional
    public ResponseEntity<?> putMethodName(@PathVariable String id, @RequestBody Labels label) {
        try {
           // Labels label2 = labelServicesImpl.getLabelById(Integer.parseInt(id));
            labelServicesImpl.updateLabel(label, Integer.parseInt(id));
            return ResponseEntity.ok("Label updated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<?> deleteLabel(@PathVariable int id) {
        try {
            labelServicesImpl.deleteLabel(id);
            return ResponseEntity.ok("Label deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
}
