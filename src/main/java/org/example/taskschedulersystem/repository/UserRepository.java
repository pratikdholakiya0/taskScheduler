package org.example.taskschedulersystem.repository;

import org.bson.types.ObjectId;
import org.example.taskschedulersystem.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findUserByUsername(String username);

    User findUserById(ObjectId id);
}
