package org.example.taskschedulersystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User {
    private ObjectId id;
    @Indexed(unique = true)
    private String username;
    private String fullName;
    private String password;
    private String email;
    private byte[] image;
    private Set<String> roles = new HashSet<>();
    private Date created_at;
    private Date updated_at;
    @DBRef
    private List<Tasks> tasks = new ArrayList<>();
}