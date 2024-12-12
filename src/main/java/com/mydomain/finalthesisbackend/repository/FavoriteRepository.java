package com.mydomain.finalthesisbackend.repository;

import com.mydomain.finalthesisbackend.model.Favorite;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FavoriteRepository extends MongoRepository<Favorite, String> {
    Favorite findByUserId(String userId);
}
