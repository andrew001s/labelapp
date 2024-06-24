package com.golden.labelapp.labelapp.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.golden.labelapp.labelapp.models.entities.Labels;

/**
 * Esta interfaz representa un repositorio de etiquetas.
 * Proporciona métodos para acceder y manipular las etiquetas en la base de datos.
 */
public interface LabelsRepository extends MongoRepository<Labels, Integer> {
    /**
     * Busca y devuelve una lista de etiquetas que coinciden con la clase de etiqueta especificada.
     * 
     * @param labelclass la clase de etiqueta a buscar
     * @return una lista de etiquetas que coinciden con la clase de etiqueta especificada
     */
    List<Labels> findByLabel(String labelclass);

    /**
     * Devuelve la etiqueta con el ID especificado.
     * 
     * @param id el ID de la etiqueta a buscar
     * @return la etiqueta con el ID especificado
     */
    Labels getLabelById(int id);

    /**
     * Devuelve la etiqueta con la etiqueta especificada.
     * 
     * @param label la etiqueta a buscar
     * @return la etiqueta con la etiqueta especificada
     */
    Labels getLabelByLabel(String label);

    /**
     * Devuelve una lista de etiquetas que pertenecen a la categoría especificada.
     * 
     * @param categoria la categoría de etiqueta a buscar
     * @return una lista de etiquetas que pertenecen a la categoría especificada
     */
    List<Labels> getLabelByCategoria(String categoria);

    /**
     * Devuelve la etiqueta con la subcategoría especificada y que no sea un logo.
     * 
     * @param subcategoria la subcategoría de etiqueta a buscar
     * @return la etiqueta con la subcategoría especificada y que no sea un logo
     */
    @Query("{'$and':[{'subcategoria': ?0}, {'isLogo': false}]}")
    Labels getLabelBySubcategoria(String subcategoria);
}
