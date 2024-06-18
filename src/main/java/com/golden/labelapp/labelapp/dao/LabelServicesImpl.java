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

import com.golden.labelapp.labelapp.dto.DetailsDto;
import com.golden.labelapp.labelapp.dto.Labels;
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
    private MongoTemplate mongoTemplate;

    /**
     * Obtiene todas las etiquetas existentes.
     * 
     * @return una lista de objetos Labels que representan todas las etiquetas
     *         existentes.
     */
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
    public void saveLabel(String labelclass) {
        List<Labels> existingLabels = labelsRepository.findByLabel(labelclass);
        int id = 0;
        int cant = 0;
        if (!existingLabels.isEmpty()) {
            id = existingLabels.get(0).getId();
            Labels existingLabel = existingLabels.get(0);
            existingLabel.setCant(existingLabel.getCant() + 1);
            Query query = new Query(Criteria.where("id").is(id));
            Update update = new Update().set("cant", existingLabel.getCant());
            mongoTemplate.updateFirst(query, update, Labels.class);

        } else {

            cant = 1;
            List<Labels> allLabels = labelsRepository.findAll();
            if (allLabels.isEmpty()) {
                id = 0;
            } else {
                id = labelsRepository.findAll().get(labelsRepository.findAll().size() - 1).getId() + 1;
            }
            Labels newLabel = new Labels(id, labelclass, cant);
            labelsRepository.save(newLabel);
        }
    }

    /**
     * Obtiene el ID de una etiqueta por su clase.
     * 
     * @param labelclass la clase de la etiqueta.
     * @return el ID de la etiqueta si existe, de lo contrario, -1.
     */
    @Override
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
    public Labels getLabelById(int id) {
        return labelsRepository.getLabelById(id);
    }

    /**
     * Obtiene una etiqueta por su nombre.
     * 
     * @param name el nombre de la etiqueta.
     * @return un objeto Labels que representa la etiqueta encontrada.
     */
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
     * 
     * @param id el ID de la etiqueta.
     * @return un objeto DetailsDto que contiene los detalles de la etiqueta.
     */
    @Override
    public DetailsDto getDetails(String categoria, int minNumImg) {
        List<Labels> label = labelsRepository.getLabelByCategoria(categoria);
        DetailsDto details = null;
        Map<String,Integer> subcategorias=new HashMap<>();
        Map<String,Integer> logos=new HashMap<>();
        for (Labels l : label) {
            subcategorias.put(l.getSubcategoria(),l.getCant());
            if(l.isLogo()){
                logos.put(l.getLabel(),l.getCant());
            }
        }
        details = new DetailsDto(categoria, subcategorias, logos);

        return details;

    }
}
