package com.golden.labelapp.labelapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.golden.labelapp.labelapp.dto.Labels;
import com.golden.labelapp.labelapp.repositories.LabelsRepository;
import com.golden.labelapp.labelapp.services.LabelServices;

@Service
public class LabelServicesImpl implements LabelServices {
    @Autowired
    private LabelsRepository labelsRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Labels> getAllLabels() {
        return (List<Labels>) labelsRepository.findAll();
    }

    @Override
    public void saveLabel(String labelclass) {
        List<Labels> existingLabels = labelsRepository.findByLabel(labelclass);
        int id =0;
        int cant=0;
        if (!existingLabels.isEmpty()) {
            id = existingLabels.get(0).getId();
            Labels existingLabel = existingLabels.get(0);
            existingLabel.setCant(existingLabel.getCant() + 1); 
            Query query = new Query(Criteria.where("id").is(id));
            Update update = new Update().set("cant", existingLabel.getCant());
            mongoTemplate.updateFirst(query, update, Labels.class);
            
        } else {
            
            cant = 1;
            id = labelsRepository.findAll().get(labelsRepository.findAll().size() - 1).getId() + 1;
            Labels newLabel = new Labels(id,labelclass, cant);
            labelsRepository.save(newLabel); 
        }
    }

    @Override
    public int getLabelId(String labelclass) {
        List<Labels> existingLabels = labelsRepository.findByLabel(labelclass);
        if (!existingLabels.isEmpty()) {
            return existingLabels.get(0).getId();
        } else {
            return -1;
        }
    }

}
