package com.golden.labelapp.labelapp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.golden.labelapp.labelapp.dto.Validation;

public interface ValidationRepository extends MongoRepository<Validation, String>{

}
