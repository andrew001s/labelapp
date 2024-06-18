package com.golden.labelapp.labelapp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.golden.labelapp.labelapp.models.entities.Validation;

public interface ValidationRepository extends MongoRepository<Validation, String>{

}
