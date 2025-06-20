package org.example.taskschedulersystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.example.taskschedulersystem.model.Tasks;
import org.example.taskschedulersystem.model.User;
import org.example.taskschedulersystem.repository.UserRepository;
import org.example.taskschedulersystem.service.EmailService;
import org.example.taskschedulersystem.service.TaskService;
import org.example.taskschedulersystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @PostMapping("/create-task")
    public ResponseEntity<Tasks> createTask(@RequestBody Tasks tasks) throws Exception{
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Tasks created = taskService.createTask(tasks, username);
            return new ResponseEntity<>(tasks, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating task: ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-task/{id}")
    public ResponseEntity<Tasks> updateTask(@RequestBody Tasks tasks, @PathVariable ObjectId id){
        try {
            taskService.upadateTask(tasks, id);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @PostMapping("/completion/{id}")
    public void completeTask(@PathVariable ObjectId id){
        taskService.completeTask(id);
    }

    @GetMapping("/get-All-Tasks")
    public List<Tasks> getAll(){
        return taskService.getAll();
    }

    public static void main(String[] args) {

        HashSet<Integer> set = new HashSet<>();
    }

    @DeleteMapping("/delete-task/{id}")
    public ResponseEntity<Tasks> deleteTask(@RequestBody Tasks tasks, @PathVariable ObjectId id){
        try {
            taskService.deleteTask(id);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }
}
