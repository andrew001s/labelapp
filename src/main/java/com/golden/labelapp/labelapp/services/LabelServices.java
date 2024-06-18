package com.golden.labelapp.labelapp.services;

import java.util.List;
import java.util.Optional;

import com.golden.labelapp.labelapp.models.dtos.DetailsDto;
import com.golden.labelapp.labelapp.models.entities.Labels;

/**
 * Esta interfaz define los métodos para acceder y manipular las etiquetas en la aplicación.
 */
public interface LabelServices {
    
    /**
     * Obtiene todas las etiquetas existentes.
     * 
     * @return una lista de objetos Labels que representan las etiquetas.
     */
    List<Labels> getAllLabels();
    
    /**
     * Guarda una nueva etiqueta en la base de datos.
     * 
     * @param labelclass la clase de la etiqueta a guardar.
     */
    void saveLabel(String labelclass);
    
    /**
     * Obtiene el ID de una etiqueta dado su nombre de clase.
     * 
     * @param labelclass el nombre de clase de la etiqueta.
     * @return el ID de la etiqueta.
     */
    int getLabelId(String labelclass);
    
    /**
     * Obtiene una etiqueta dado su ID.
     * 
     * @param id el ID de la etiqueta.
     * @return un objeto Labels que representa la etiqueta.
     */
    Labels getLabelById(int id);
    
    /**
     * Obtiene una etiqueta dado su nombre de clase.
     * 
     * @param name el nombre de clase de la etiqueta.
     * @return un objeto Labels que representa la etiqueta.
     */
    Labels getLabelByName(String name);
    
    /**
     * Elimina una etiqueta dado su ID.
     * 
     * @param id el ID de la etiqueta a eliminar.
     */
    void deleteLabel(int id);
    
    /**
     * Actualiza una etiqueta existente.
     * 
     * @param label el objeto Labels que representa la etiqueta actualizada.
     * @param id el ID de la etiqueta a actualizar.
     * @return un objeto Optional que contiene la etiqueta actualizada, o vacío si no se encontró la etiqueta.
     */
    Optional<Labels> updateLabel(Labels label, int id);
    
    DetailsDto getDetails(String categoria,int minNumImg);

}
