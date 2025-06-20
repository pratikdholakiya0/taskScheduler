package org.example.taskschedulersystem.repository;

import org.bson.types.ObjectId;
import org.example.taskschedulersystem.model.Tasks;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Tasks, ObjectId> {
    Tasks findTasksById(ObjectId id);

    void deleteTasksById(ObjectId id);
}