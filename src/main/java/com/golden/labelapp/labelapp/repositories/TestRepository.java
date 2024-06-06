package com.golden.labelapp.labelapp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.golden.labelapp.labelapp.dto.Test;

public interface TestRepository extends MongoRepository<Test, String>{}
