package org.example.taskschedulersystem.service;

import org.example.taskschedulersystem.model.Tasks;
import org.example.taskschedulersystem.model.User;
import org.example.taskschedulersystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user){
        File file = new File("C:\\Users\\dhola\\Downloads\\SpringBootProjects\\TaskSchedulerSystem\\src\\main\\resources\\templates\\shreekrishna.png");
        try {
            InputStream inputStream = new FileInputStream(file);
            user.setImage(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userRepository.save(user);
    }

    public User getUser(String username){
        return userRepository.findUserByUsername(username);
    }

    public List<Tasks> getTasks(String username){
        return getUser(username).getTasks();
    }
}
