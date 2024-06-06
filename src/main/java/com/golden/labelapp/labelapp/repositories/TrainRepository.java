package com.golden.labelapp.labelapp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.golden.labelapp.labelapp.dto.Train;

public interface TrainRepository extends MongoRepository<Train, String>{

}
