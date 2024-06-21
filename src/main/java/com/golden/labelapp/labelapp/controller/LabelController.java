package com.golden.labelapp.labelapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golden.labelapp.labelapp.models.dtos.DetailsDto;
import com.golden.labelapp.labelapp.models.entities.Image;
import com.golden.labelapp.labelapp.models.entities.Labels;
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
@CrossOrigin(originPatterns = "*")
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
    @GetMapping("/getid/{name}")
    public int getLabelByName(@PathVariable String name) {
        return labelServicesImpl.getLabelId(name);
    }

    /**
     * Inserta una nueva etiqueta.
     * 
     * @param label la etiqueta a insertar
     * @return un mensaje indicando que la etiqueta ha sido insertada o un mensaje de error en caso de fallo
     */
    @PostMapping("/insertLabel")
    public String insertLabel(@RequestBody Labels label) {
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
            DetailsDto details = labelServicesImpl.getDetails(categoria, minNumImg);
            if (details == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
}
