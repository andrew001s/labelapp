package com.golden.labelapp.labelapp.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.golden.labelapp.labelapp.dto.Image;

public interface ImageRespository extends MongoRepository<Image, Integer> {
    @SuppressWarnings("null")
    Page<Image> findAll(Pageable pageable);
    

}
