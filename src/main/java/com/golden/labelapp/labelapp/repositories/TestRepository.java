package com.golden.labelapp.labelapp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.golden.labelapp.labelapp.models.entities.Test;

public interface TestRepository extends MongoRepository<Test, String>{}
