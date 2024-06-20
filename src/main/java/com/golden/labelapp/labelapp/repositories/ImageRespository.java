package com.golden.labelapp.labelapp.repositories;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.golden.labelapp.labelapp.models.entities.Image;

public interface ImageRespository extends MongoRepository<Image, Integer> {
    @SuppressWarnings("null")
    Page<Image> findAll(Pageable pageable);
    @Query("{'ids': { $in: ?0 }}")
    List<Image> findByIds(int[] ids);
    @Query("{'ids': { $elemMatch: {$eq:?0 }}}")
    List<Image> findByIdsContaing(int id);
    @Query("{'createdAt': { $gte: ?0, $lte: ?1 }}")
    List<Image> findByCreatedAtBetween(Date startDate, Date endDate);
    @Query("{'updatedAt': { $gte: ?0, $lte: ?1 }}")
    List<Image> findByUpdatedAtBetween(Date startDate, Date endDate);
    @Query(value = "{}", sort = "{_id : -1}")
    Optional<Image> findFirstByOrderByIdDesc();
}
