/*
will allow you to fetch user details by username, which is essential for authentication.
*/ 
package com.mydomain.finalthesisbackend.repository;

import com.mydomain.finalthesisbackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByusername(String username);
}
