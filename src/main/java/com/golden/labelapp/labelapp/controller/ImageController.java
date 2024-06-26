package com.golden.labelapp.labelapp.controller;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import com.golden.labelapp.labelapp.models.entities.Labels;
import com.golden.labelapp.labelapp.services.ImageServices;
import com.golden.labelapp.labelapp.services.LabelServices;

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
    @Autowired
    private LabelServices labelServicesImpl;
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
            Image image = imageServicesImpl.insertImage(img);
            saveLabel();
            return ResponseEntity.status(HttpStatus.CREATED).body(image);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Guarda las etiquetas extraídas de las imágenes.
     * 
     * @return un mensaje indicando que la operación ha finalizado
     */
    public void saveLabel() {
        List<Image> images = imageServicesImpl.getAllImages();
        for (Object element : images) {
            for (Object element2 : ((Image) element).getShapes()) {
                @SuppressWarnings("unchecked")
                String label = (String) ((Map<String, Object>) element2).get("label");
                Labels Logo= labelServicesImpl.getLabelByName(label);
                if (Logo != null) {
                    labelServicesImpl.saveLabel(Logo);
                    Labels SubCate= labelServicesImpl.getLabelSubcategoria(label);
                    labelServicesImpl.saveLabel(SubCate);
                } else {
                    Labels SubCate= labelServicesImpl.getLabelSubcategoria(label);
                    labelServicesImpl.saveLabel(SubCate);
                } 

            }
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
            int id = 1;
            //obtiene el id de la ultima imagen
            if (imageServicesImpl.getAllImages().size() > 0) {
                id = imageServicesImpl.getAllImages().get(imageServicesImpl.getAllImages().size() - 1).getId() + 1;
            }
            // Itera sobre la lista de archivos
            for (MultipartFile file : files) {
                // Obtiene la extensión del archivo
                String filename = file.getOriginalFilename();
                String extension = "";
                if (filename != null) {
                    extension = filename.substring(filename.lastIndexOf("."));
                }
                // Crea la URL de la imagen
                String url = id + "" + extension;
                // Convierte el archivo a base64
                byte[] bytes = file.getBytes();
                byte[] encoded = Base64.getEncoder().encode(bytes);
                String encodedString = new String(encoded);
                // Crea el JSON con los datos de la imagen
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
                // Sube la imagen al servidor
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
    /**
     * Obtiene las imagenes filtrado por fecha de creación.
     * 
     * @param startDate La fecha de inicio. 
     * @param endDate La fecha de fin.
     * @return Lista de imagenes filtradas por fecha.
     */
    @GetMapping("/getByCreated")
    public List<Image> getByCreated(@RequestParam String startDate, @RequestParam String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        startDate = startDate.replace(" ", "+");
        endDate = endDate.replace(" ", "+");
        ZonedDateTime start = ZonedDateTime.parse(startDate, formatter);
        ZonedDateTime end = ZonedDateTime.parse(endDate, formatter);
        return imageServicesImpl.getImageByCreatedDate(Date.from(start.toInstant()), Date.from(end.toInstant()));
    }

    /**
     * Obtiene las imagenes filtrado por fechas de actualización.
     * 
     * @param startDate La fecha de inicio. 
     * @param endDate La fecha de fin.
     * @return Lista de imagenes filtradas por fecha de actualización.
     */
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
