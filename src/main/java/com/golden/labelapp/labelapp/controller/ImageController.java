package com.golden.labelapp.labelapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.golden.labelapp.labelapp.dto.Image;
import com.golden.labelapp.labelapp.services.ImageServices;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageServices imageServicesImpl;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    @PostMapping("/insert")
    public ResponseEntity<?> insertImage(@RequestBody Image img) {
        try {
            
            return ResponseEntity.status(HttpStatus.CREATED).body(imageServicesImpl.extractInfoFromJson(img));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @GetMapping("/all")
    public ResponseEntity<?> getAllImages() {
        try {
            
            return ResponseEntity.ok(imageServicesImpl.getAllImages());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getImageById(@PathVariable int id) {
        try {
            
            return ResponseEntity.ok(imageServicesImpl.getImageById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable int id) {
        try {
            imageServicesImpl.deleteImage(id);
            return ResponseEntity.ok("Image deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("files") List<MultipartFile> files) {
        try {
            return ResponseEntity.ok(imageServicesImpl.uploadImage(files, uploadDir));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    
    

}
