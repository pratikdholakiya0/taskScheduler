package org.example.taskschedulersystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tasks")
public class Tasks {
    @MongoId
    private ObjectId id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    private String status;
    private Date created_at;
    private Date updated_at;
    private Date due_date;
    @NonNull
    private String assigned_to;
    @NonNull
    private ObjectId assigned_by;
}
