package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Dislike;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDislikeRepository extends MongoRepository<Dislike, String> {
}
