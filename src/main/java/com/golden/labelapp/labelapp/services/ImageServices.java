package com.golden.labelapp.labelapp.services;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.golden.labelapp.labelapp.models.entities.Image;
import com.golden.labelapp.labelapp.models.entities.Labels;

/**
 * Esta interfaz define los métodos para el servicio de imágenes.
 */
public interface ImageServices {
    
    /**
     * Extrae información de una imagen en formato JSON.
     * 
     * @param img La imagen en formato JSON.
     * @return Un mapa con la información extraída.
     */
    Map<String, Object> extractInfoFromJson(Image img);
    
    /**
     * Inserta una imagen en la base de datos.
     * 
     * @param img La imagen a insertar.
     * @return La imagen insertada.
     */
    Image insertImage(Image img);
    
    /**
     * Obtiene todas las imágenes almacenadas en la base de datos paginados.
     * 
     * @return Una lista de todas las imágenes.
     */
    Page<Image> getPageImages(int page, int size);
    
    /**
     * Actualiza una imagen existente en la base de datos.
     * 
     * @param img La imagen actualizada.
     * @param id El ID de la imagen a actualizar.
     * @return Un objeto Optional que contiene la imagen actualizada, si existe.
     */
    Optional<Image> updateImage(Image img, int id);
    
    /**
     * Obtiene una imagen por su ID.
     * 
     * @param id El ID de la imagen.
     * @return Un objeto Optional que contiene la imagen, si existe.
     */
    Optional<Image> getImageById(int id);
    
    /**
     * Elimina una imagen de la base de datos.
     * 
     * @param id El ID de la imagen a eliminar.
     */
    void deleteImage(int id);
    
    /**
     * Convierte la información de una imagen al formato YOLOv5.
     * 
     * @param info_dict Un mapa con la información de la imagen.
     * @param height La altura de la imagen.
     * @param width El ancho de la imagen.
     * @param name El nombre de la imagen.
     * @param labels Una lista de etiquetas asociadas a la imagen.
     */
    void convertToYoloV5(Map<String, Object> info_dict, int height, int width, String name, List<Labels> labels,int num_labels);
    
    /**
     * Sube una imagen al servidor.
     * 
     * @param file La imagen a subir.
     * @param path La ruta donde se almacenará la imagen.
     * @return Un mapa con la información de la imagen subida.
     */
    Map<String, Object> uploadImage(List<MultipartFile> file, String path);

    /**
     * Obtiene todas las imágenes almacenadas en la base de datos.
     * 
     * @return Una lista de todas las imágenes.
     */
    List<Image> getAllImages();
    /**
     * Obtiene una imagen por su fecha de creacion.
     * 
     * @param startDate La fecha de inicio.
     * @param endDate La fecha de fin.
     * @return Un objeto Optional que contiene la imagen, si existe.
     */
    List<Image> getImageByCreatedDate(Date startDate, Date endDate);
    /**
     * Obtiene una imagen por su fecha de actualizacion.
     * 
     * @param startDate La fecha de inicio.
     * @param endDate La fecha de fin.
     * @return Un objeto Optional que contiene la imagen, si existe.
     */
    List<Image> getImageByUpdatedDate(Date startDate, Date endDate);
    /**
     * Obtiene la ultima imagen almacenada en la base de datos.
     * 
     * @return Un objeto Optional que contiene la imagen, si existe.
     */
    Optional<Image> getLastId();
}
