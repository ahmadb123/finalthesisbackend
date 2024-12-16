/*
 * CartRepository extends mongorepository
 */
package com.mydomain.finalthesisbackend.repository;

import com.mydomain.finalthesisbackend.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface CartRepository extends MongoRepository<Cart, String> {
    Cart findByUserId(String userId);
}
