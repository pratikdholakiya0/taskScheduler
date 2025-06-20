package org.example.taskschedulersystem.service;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.example.taskschedulersystem.enums.Status;
import org.example.taskschedulersystem.model.Tasks;
import org.example.taskschedulersystem.model.User;
import org.example.taskschedulersystem.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    public Tasks getTasks(ObjectId id){
        return taskRepository.findTasksById(id);
    }

    public List<Tasks> getAll(){
        return taskRepository.findAll();
    }

    @Transactional
    public Tasks createTask(Tasks tasks, String username){
        User userAdmin = userService.getUser(username);
        User userEmployee = userService.getUser(tasks.getAssigned_to());
        tasks.setStatus(Status.PENDING);
        tasks.setCreated_at(new Date(System.currentTimeMillis()));
        tasks.setDue_date(new Date(System.currentTimeMillis() + 1000*60*60*24*7));
        tasks.setAssigned_by(userAdmin.getId());
        taskRepository.save(tasks);
        userEmployee.getTasks().add(tasks);
        userService.saveUser(userEmployee);
        emailService.sendEmail(userEmployee, "task assigned", "C:/Users/dhola/Downloads/SpringBootProjects/TaskSchedulerSystem/src/main/resources/templates/EmailTemplete.html");
        return tasks;
    }

    @Transactional
    public void upadateTask(Tasks tasks, ObjectId id){
        try {
            Tasks taskDb = getTasks(id);
            String oldperson = taskDb.getAssigned_to();
            taskDb.setAssigned_to(tasks.getAssigned_to());
            taskDb.setStatus(Status.IN_PROGRESS);
            taskDb.setTitle(tasks.getTitle());
            taskDb.setDue_date(tasks.getDue_date());
            taskDb.setUpdated_at(new Date(System.currentTimeMillis()));
            taskDb.setDescription(tasks.getDescription());
            taskDb.setStatus(tasks.getStatus());
            taskRepository.save(taskDb);
            User user = userService.getUser(taskDb.getAssigned_to());
            emailService.sendEmail(user, "task updated","C:/Users/dhola/Downloads/SpringBootProjects/TaskSchedulerSystem/src/main/resources/templates/EmailTemplete.html");
            if (!tasks.getAssigned_to().equals(oldperson)){
                User olduser = userService.getUser(oldperson);
                emailService.sendEmail(olduser, "you are free from task with id " + taskDb.getId() ,"C:/Users/dhola/Downloads/SpringBootProjects/TaskSchedulerSystem/src/main/resources/templates/EmailTemplete.html");
            }
        } catch (Exception e) {
            log.error("error occurred during task updating");
        }
    }

    @Transactional
    public void completeTask(ObjectId id){
        try {
            Tasks tasks = taskRepository.findTasksById(id);
            tasks.setStatus(Status.COMPLETED);
            upadateTask(tasks, id);
        } catch (Exception e) {
            log.error("Exception occurred");
            throw new RuntimeException();
        }
    }

    @Transactional
    public void deleteTask(ObjectId id){
        taskRepository.deleteTasksById(id);
    }
}
