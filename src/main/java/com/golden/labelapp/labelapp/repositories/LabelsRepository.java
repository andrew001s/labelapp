package com.golden.labelapp.labelapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.golden.labelapp.labelapp.models.entities.Labels;

public interface LabelsRepository extends MongoRepository<Labels, Integer> {
    List<Labels> findByLabel(String labelclass);

    Labels getLabelById(int id);

    Labels getLabelByLabel(String label);

    List<Labels> getLabelByCategoria(String categoria);

}
