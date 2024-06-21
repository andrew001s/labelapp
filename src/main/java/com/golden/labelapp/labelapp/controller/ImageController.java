package com.golden.labelapp.labelapp.controller;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.golden.config.BackBlaze;
import com.golden.labelapp.labelapp.models.entities.Image;
import com.golden.labelapp.labelapp.services.ImageServices;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controlador para la gestión de imágenes.
 */
@RestController
@CrossOrigin(originPatterns = "*")
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
            int id = 0;
            if (imageServicesImpl.getAllImages().size() > 0) {
                id = imageServicesImpl.getAllImages().get(imageServicesImpl.getAllImages().size() - 1).getId() + 1;
            }
            for (MultipartFile file : files) {

                String filename = file.getOriginalFilename();
                String extension = filename.substring(filename.lastIndexOf("."));
                String url = id + "" + extension;
                byte[] bytes = file.getBytes();
                byte[] encoded = Base64.getEncoder().encode(bytes);
                String encodedString = new String(encoded);
                String type = BackBlaze.type;
                String network = BackBlaze.network;
                boolean avatar = BackBlaze.avatar;
                boolean scan = BackBlaze.scan;
                String base64 = encodedString;
                String jsonData = "{\n" +
                        "    \"id\": \"" + String.valueOf(id) + "\",\n" +
                        "    \"url\": \"" + url + "\",\n" +
                        "    \"base64\": \"" + base64 + "\",\n" +
                        "    \"type\":\"" + type + "\",\n" +
                        "    \"name\": \"" + filename + "\",\n" +
                        "    \"network\": \"" + network + "\",\n" +
                        "    \"avatar\": \"" +avatar + "\",\n" +
                        "    \"scan\": \"" + scan + "\"\n" +
                        "}";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<String>(jsonData, headers);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.exchange(uploadDir, HttpMethod.POST, entity,String.class);
                
            }
            return ResponseEntity.ok("Imagenes subida correctamente");
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
    @GetMapping("/getImagePage")
    public ResponseEntity<?> getPageImages(@RequestParam int page, @RequestParam int size) {
        try {
            return ResponseEntity.ok(imageServicesImpl.getPageImages(page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/getByCreated")
    public List<Image> getByCreated(@RequestParam String startDate, @RequestParam String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        startDate = startDate.replace(" ", "+");
        endDate = endDate.replace(" ", "+");
        ZonedDateTime start = ZonedDateTime.parse(startDate, formatter);
        ZonedDateTime end = ZonedDateTime.parse(endDate, formatter);
        return imageServicesImpl.getImageByCreatedDate(Date.from(start.toInstant()), Date.from(end.toInstant()));
    }

    @GetMapping("/getByUpdated")
    public List<Image> getByUpdated(@RequestParam String startDate, @RequestParam String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        startDate = startDate.replace(" ", "+");
        endDate = endDate.replace(" ", "+");
        ZonedDateTime start = ZonedDateTime.parse(startDate, formatter);
        ZonedDateTime end = ZonedDateTime.parse(endDate, formatter);
        return imageServicesImpl.getImageByUpdatedDate(Date.from(start.toInstant()), Date.from(end.toInstant()));
    }

}
