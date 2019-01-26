package com.baagir.shopping.repository;

import com.baagir.shopping.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<User, UUID> {
//    public void updateUserPassword(User user);
}
