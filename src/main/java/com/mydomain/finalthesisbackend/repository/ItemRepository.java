package com.mydomain.finalthesisbackend.repository;

import com.mydomain.finalthesisbackend.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, String> {
    // Additional query methods, if needed, can be added here.
}
