package com.golden.labelapp.labelapp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.golden.labelapp.labelapp.models.dtos.DetailsDto;
import com.golden.labelapp.labelapp.models.entities.Labels;
import com.golden.labelapp.labelapp.repositories.ImageRespository;
import com.golden.labelapp.labelapp.repositories.LabelsRepository;
import com.golden.labelapp.labelapp.services.LabelServices;

/**
 * Implementación de la interfaz LabelServices que proporciona métodos para
 * realizar operaciones relacionadas con las etiquetas.
 */
@Service
public class LabelServicesImpl implements LabelServices {
    @Autowired
    private LabelsRepository labelsRepository;
    @Autowired
    private ImageRespository imageRespository;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Obtiene todas las etiquetas existentes.
     * 
     * @return una lista de objetos Labels que representan todas las etiquetas
     *         existentes.
     */
    @Transactional(readOnly = true)
    @Override
    public List<Labels> getAllLabels() {
        return (List<Labels>) labelsRepository.findAll();
    }

    /**
     * Guarda una nueva etiqueta o actualiza una existente.
     * 
     * @param labelclass la clase de la etiqueta a guardar o actualizar.
     */
    @Override
    @Transactional
    public void saveLabel(Labels labelclass) {
        // Si la etiqueta es un logo, se guarda con el nombre de la etiqueta y la
        if (labelclass.isLogo()) {
            // Si la etiqueta es un logo, se guarda con el nombre de la etiqueta 
            List<Labels> existingLabels = labelsRepository.findByLabel(labelclass.getLabel());
            int[] id= idLabel(existingLabels);
            // Si la etiqueta ya existe, se actualiza la cantidad de imágenes que tiene asociadas
            if (id != null) {
                // Si la etiqueta ya existe, se actualiza la cantidad de imágenes que tiene asociadas
                Labels newLabel = new Labels(id[0],labelclass.getLabel(), id[1],labelclass.isLogo(),  labelclass.getCategoria(),
                        labelclass.getSubcategoria());
                labelsRepository.save(newLabel);
            }
        }
        else {
            // Si la etiqueta no es un logo, se guarda con el nombre de la subcategoría
            List<Labels> existingLabels =  new ArrayList<>();
            if (labelsRepository.getLabelBySubcategoria(labelclass.getSubcategoria()) != null)
            {
                existingLabels.add(labelsRepository.getLabelBySubcategoria(labelclass.getSubcategoria()));
            }
            int[] id= idLabel(existingLabels);
            // Si la etiqueta ya existe, se actualiza la cantidad de imágenes que tiene asociadas
            if (id != null) {
                Labels newLabel = new Labels(id[0],labelclass.getLabel(), id[1],labelclass.isLogo(),  labelclass.getCategoria(),
                        labelclass.getSubcategoria());
                labelsRepository.save(newLabel);
            }
        }

    }

    /**
     * Obtiene el ID de una etiqueta y actualiza la cantidad de imágenes que tiene
     * asociadas.
     * 
     * @param existingLabels la lista de etiquetas existentes.
     * @return un arreglo de enteros que contiene el ID de la etiqueta y la cantidad
     *         de imágenes que tiene asociadas.
     */
    public int[] idLabel( List<Labels> existingLabels) {
        
        int id = 0;
        int cant = 0;
        // Si la etiqueta ya existe, se actualiza la cantidad de imágenes que tiene asociadas
        if (!existingLabels.isEmpty()) {
            id = existingLabels.get(0).getId();
            Labels existingLabel = existingLabels.get(0);
            existingLabel.setCant(existingLabel.getCant() + 1);
            Query query = new Query(Criteria.where("id").is(id));
            Update update = new Update().set("cant", existingLabel.getCant());
            mongoTemplate.updateFirst(query, update, Labels.class);

        } else {
            // Si la etiqueta no existe, se crea una nueva
            cant = 0;
            List<Labels> allLabels = labelsRepository.findAll();
            if (allLabels.isEmpty()) {
                id = 0;
            } else {
                id = labelsRepository.findAll().get(labelsRepository.findAll().size() - 1).getId() + 1;
            }
            return new int[] { id, cant };
        }
        return null;
    }
    /**
     * Obtiene el ID de una etiqueta por su clase.
     * 
     * @param labelclass la clase de la etiqueta.
     * @return el ID de la etiqueta si existe, de lo contrario, -1.
     */
    @Override
    @Transactional(readOnly = true)
    public int getLabelId(String labelclass) {
        List<Labels> existingLabels = labelsRepository.findByLabel(labelclass);
        if (!existingLabels.isEmpty()) {
            return existingLabels.get(0).getId();
        } else {
            return -1;
        }
    }

    /**
     * Obtiene una etiqueta por su ID.
     * 
     * @param id el ID de la etiqueta.
     * @return un objeto Labels que representa la etiqueta encontrada.
     */
    @Override
    @Transactional(readOnly = true)
    public Labels getLabelById(int id) {
        return labelsRepository.getLabelById(id);
    }

    /**
     * Obtiene una etiqueta por su nombre.
     * 
     * @param name el nombre de la etiqueta.
     * @return un objeto Labels que representa la etiqueta encontrada.
     */
    @Transactional(readOnly = true)
    @Override
    public Labels getLabelByName(String name) {
        return labelsRepository.getLabelByLabel(name);
    }

    /**
     * Elimina una etiqueta por su ID.
     * 
     * @param id el ID de la etiqueta a eliminar.
     */
    @Override
    @Transactional
    public void deleteLabel(int id) {
        labelsRepository.deleteById(id);
    }

    /**
     * Actualiza una etiqueta existente.
     * 
     * @param label el objeto Labels que contiene los nuevos datos de la etiqueta.
     * @param id    el ID de la etiqueta a actualizar.
     * @return un objeto Optional que contiene la etiqueta actualizada si existe, de
     *         lo contrario, vacío.
     */
    @Override
    @Transactional
    public Optional<Labels> updateLabel(Labels label, int id) {
        Optional<Labels> existingLabel = labelsRepository.findById(id);
        if (existingLabel.isPresent()) {
            Update update = new Update();
            update.set("label", label.getLabel());
            update.set("cant", label.getCant());
            Query query = Query.query(Criteria.where("_id").is(id));
            Labels updatedLabel = mongoTemplate.findAndModify(query, update, Labels.class);
            return Optional.of(updatedLabel);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Obtiene los detalles de una etiqueta por su ID.
     * @param categoria la categoria de la etiqueta.
     * @param minNumImg el número mínimo de imágenes que debe tener la etiqueta.
     * @return un objeto DetailsDto que contiene los detalles de la etiqueta.
     */
    @Transactional(readOnly = true)
    @Override
    public DetailsDto getDetails(String categoria, int minNumImg) {
        // Obtiene todas las etiquetas de una categoría
        List<Labels> labels = labelsRepository.getLabelByCategoria(categoria);
        Map<String, Integer> subcategorias = new HashMap<>();
        Map<String, Integer> logos = new HashMap<>();
        int totalImgs = 0;
        int cantSubcat = 0;
        int cantLogos = 0;
        int[] ids = new int[labels.size()];
        // Recorre las etiquetas
        for (Labels label : labels) {
            // Guarda los IDs de las etiquetas
            ids[labels.indexOf(label)] = label.getId();
            // Obtiene la subcategoría, la cantidad de imágenes y si es un logo
            String subcategoria = label.getSubcategoria();
            boolean isLogo = label.isLogo();
            // Obtiene la cantidad de imágenes asociadas a la subcategoría
            cantSubcat = imageRespository.findByIdsContaing(ids[labels.indexOf(label)]).size();
            // Verifica si la cantidad de imágenes es mayor o igual al número mínimo de imágenes
            // y si no es un logo
            if (cantSubcat >= minNumImg && !isLogo) {
                subcategorias.put(subcategoria, cantSubcat);
                totalImgs += cantSubcat;

            }
            // Verifica si la cantidad de imágenes es mayor o igual al número mínimo de imágenes
            // y si es un logo
            if (isLogo) {
                // Obtiene la cantidad de imágenes asociadas al logo
                cantLogos = imageRespository.findByIdsContaing(ids[labels.indexOf(label)]).size();
                // Verifica si la cantidad de imágenes es mayor o igual al número mínimo de imágenes
                if (cantLogos >= minNumImg) {
                    logos.put(label.getLabel(), cantLogos);
                    totalImgs += cantLogos;
                }

            }

        }

        if (totalImgs < minNumImg) {
            return null;
        }

        return new DetailsDto(categoria, subcategorias, logos, totalImgs);
    }

    /**
     * Obtiene una etiqueta por su subcategoría.
     * 
     * @param subcategoria la subcategoría de la etiqueta.
     * @return un objeto Labels que representa la etiqueta encontrada.
     */
    @Override
    public Labels getLabelSubcategoria(String subcategoria) {
        return labelsRepository.getLabelBySubcategoria(subcategoria);
    }

}
