package com.golden.labelapp.labelapp.repositories;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.golden.labelapp.labelapp.models.entities.Image;

/**
 * Esta interfaz representa un repositorio para la entidad Image.
 * Proporciona métodos para realizar operaciones de consulta en la base de datos.
 */
public interface ImageRespository extends MongoRepository<Image, Integer> {

    /**
     * Recupera todas las imágenes paginadas.
     *
     * @param pageable la información de paginación
     * @return una página de imágenes
     */
    @SuppressWarnings("null")
    Page<Image> findAll(Pageable pageable);

    /**
     * Recupera las imágenes por sus IDs.
     *
     * @param ids los IDs de las imágenes a buscar
     * @return una lista de imágenes que coinciden con los IDs proporcionados
     */
    @Query("{'ids': { $in: ?0 }}")
    List<Image> findByIds(int[] ids);

    /**
     * Recupera las imágenes que contienen un ID específico.
     *
     * @param id el ID a buscar
     * @return una lista de imágenes que contienen el ID proporcionado
     */
    @Query("{'ids': { $elemMatch: {$eq:?0 }}}")
    List<Image> findByIdsContaing(int id);

    /**
     * Recupera las imágenes creadas entre dos fechas específicas.
     *
     * @param startDate la fecha de inicio
     * @param endDate la fecha de fin
     * @return una lista de imágenes creadas entre las fechas proporcionadas
     */
    @Query("{'createdAt': { $gte: ?0, $lte: ?1 }}")
    List<Image> findByCreatedAtBetween(Date startDate, Date endDate);

    /**
     * Recupera las imágenes actualizadas entre dos fechas específicas.
     *
     * @param startDate la fecha de inicio
     * @param endDate la fecha de fin
     * @return una lista de imágenes actualizadas entre las fechas proporcionadas
     */
    @Query("{'updatedAt': { $gte: ?0, $lte: ?1 }}")
    List<Image> findByUpdatedAtBetween(Date startDate, Date endDate);

    /**
     * Recupera la primera imagen ordenada por ID de forma descendente.
     *
     * @return una imagen opcional que tiene el ID más alto
     */
    @Query(value = "{}", sort = "{_id : -1}")
    Optional<Image> findFirstByOrderByIdDesc();
}
