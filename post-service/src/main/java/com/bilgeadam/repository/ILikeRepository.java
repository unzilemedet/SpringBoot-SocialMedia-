package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILikeRepository extends MongoRepository<Like, String> {

    Optional<Like> findByUserIdAndPostId(String userId, String postId);

    void deleteByUserIdAndPostId(String userId, String postId);
    void deleteAllByPostId(String postId);
}
