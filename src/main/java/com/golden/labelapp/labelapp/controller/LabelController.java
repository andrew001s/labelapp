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
import org.springframework.web.bind.annotation.RequestParam;








/**
 * Esta clase es el controlador para las operaciones relacionadas con las etiquetas.
 */
@RestController
@RequestMapping("/label")
public class LabelController {
    @Autowired
    private LabelServices labelServicesImpl;
    @Autowired
    private ImageServices imageServices;

    /**
     * Obtiene todas las etiquetas.
     * 
     * @return una lista de todas las etiquetas
     */
    @Transactional(readOnly = true)
    @GetMapping("/all")
    public List<Labels> getAllLabels() {
        return (List<Labels>) labelServicesImpl.getAllLabels();
    }

    /**
     * Obtiene el ID de una etiqueta por su nombre.
     * 
     * @param name el nombre de la etiqueta
     * @return el ID de la etiqueta
     */
    @Transactional(readOnly = true)
    @GetMapping("/getid/{name}")
    public int getLabelByName(@PathVariable String name) {
        return labelServicesImpl.getLabelId(name);
    }

    /**
     * Guarda las etiquetas extraídas de las imágenes.
     * 
     * @return un mensaje indicando que la operación ha finalizado
     */
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
    
    /**
     * Inserta una nueva etiqueta.
     * 
     * @param label la etiqueta a insertar
     * @return un mensaje indicando que la etiqueta ha sido insertada o un mensaje de error en caso de fallo
     */
    @Transactional
    @PostMapping("/insertLabel")
    public String insertLabel(@RequestBody String label) {
        try {
            labelServicesImpl.saveLabel(label);
            return "Etiqueta insertada";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Actualiza una etiqueta existente.
     * 
     * @param id el ID de la etiqueta a actualizar
     * @param label la etiqueta actualizada
     * @return una respuesta HTTP indicando que la etiqueta ha sido actualizada o un mensaje de error en caso de fallo
     */
    @PutMapping("update/{id}")
    @Transactional
    public ResponseEntity<?> putMethodName(@PathVariable String id, @RequestBody Labels label) {
        try {
            labelServicesImpl.updateLabel(label, Integer.parseInt(id));
            return ResponseEntity.ok("Etiqueta actualizada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Elimina una etiqueta existente.
     * 
     * @param id el ID de la etiqueta a eliminar
     * @return una respuesta HTTP indicando que la etiqueta ha sido eliminada o un mensaje de error en caso de fallo
     */
    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<?> deleteLabel(@PathVariable int id) {
        try {
            labelServicesImpl.deleteLabel(id);
            return ResponseEntity.ok("Etiqueta eliminada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/getDetails")
    public ResponseEntity<?> getDetails(@RequestParam String categoria, @RequestParam int minNumImg) {
       try {
           return ResponseEntity.ok(labelServicesImpl.getDetails(categoria, minNumImg));
       } catch (Exception e) {
           return ResponseEntity.badRequest().body("Error: " + e.getMessage());
       }
    }
    
}
