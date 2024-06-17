package com.golden.labelapp.labelapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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


/**
 * Controlador para la gestión de imágenes.
 */
@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageServices imageServicesImpl;

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Inserta una nueva imagen en la base de datos.
     * 
     * @param img La imagen a insertar.
     * @return ResponseEntity con el resultado de la operación.
     */
    @Transactional
    @PostMapping("/insert")
    public ResponseEntity<?> insertImage(@RequestBody Image img) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(imageServicesImpl.insertImage(img));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Obtiene todas las imágenes de la base de datos.
     * 
     * @return ResponseEntity con la lista de imágenes.
     */
    @Transactional(readOnly = true)
    @GetMapping("/all")
    public ResponseEntity<?> getAllImages() {
        try {
            return ResponseEntity.ok(imageServicesImpl.getAllImages());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Obtiene una imagen por su ID.
     * 
     * @param id El ID de la imagen.
     * @return ResponseEntity con la imagen encontrada.
     */
    @Transactional(readOnly = true)
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getImageById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(imageServicesImpl.getImageById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Elimina una imagen por su ID.
     * 
     * @param id El ID de la imagen a eliminar.
     * @return ResponseEntity con el resultado de la operación.
     */
    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<?> deleteImage(@PathVariable int id) {
        try {
            Optional<Image> image = imageServicesImpl.getImageById(id);
            if (image.isPresent()) {
                imageServicesImpl.deleteImage(id);
                return ResponseEntity.ok(HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
        return ResponseEntity.badRequest().body("Error: Imagen no encontrada");
    }

    /**
     * Sube una o varias imágenes al servidor.
     * 
     * @param files Lista de archivos a subir.
     * @return ResponseEntity con el resultado de la operación.
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("files") List<MultipartFile> files) {
        try {
            return ResponseEntity.ok(imageServicesImpl.uploadImage(files, uploadDir));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Actualiza una imagen existente en la base de datos.
     * 
     * @param id     El ID de la imagen a actualizar.
     * @param img    La imagen actualizada.
     * @param result El resultado de la validación de la imagen.
     * @return ResponseEntity con el resultado de la operación.
     */
    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateImage(@PathVariable int id, @RequestBody Image img, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Optional<Image> image = imageServicesImpl.updateImage(img, id);
        if (image.isPresent()) {
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body("Error: Imagen no encontrada");
        }
    }

    /**
     * Obtiene una página de imágenes paginadas.
     * 
     * @param page El número de página.
     * @param size El tamaño de la página.
     * @return ResponseEntity con la página de imágenes.
     */
    @Transactional(readOnly = true)
    @GetMapping("/getImagePage")
    public ResponseEntity<?> getPageImages(@RequestParam int page, @RequestParam int size) {
        try {
            return ResponseEntity.ok(imageServicesImpl.getPageImages(page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
