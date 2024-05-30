package com.golden.labelapp.labelapp.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.golden.labelapp.labelapp.dto.Labels;

public interface LabelsRepository extends MongoRepository<Labels, Integer>{
    List<Labels> findByLabel(String labelclass);
}
