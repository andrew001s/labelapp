package com.golden.labelapp.labelapp.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.golden.labelapp.labelapp.models.entities.Labels;

public interface LabelsRepository extends MongoRepository<Labels, Integer> {
    List<Labels> findByLabel(String labelclass);

    Labels getLabelById(int id);

    Labels getLabelByLabel(String label);

    List<Labels> getLabelByCategoria(String categoria);
    @Query("{'$and':[{'subcategoria': ?0}, {'isLogo': false}]}")
    Labels getLabelBySubcategoria(String subcategoria);
    

}
