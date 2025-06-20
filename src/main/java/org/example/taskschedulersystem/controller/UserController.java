package org.example.taskschedulersystem.controller;

import org.example.taskschedulersystem.model.Tasks;
import org.example.taskschedulersystem.model.User;
import org.example.taskschedulersystem.service.UserService;
import org.example.taskschedulersystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @GetMapping("/get-All-Tasks")
    public List<Tasks> getAllTask(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.getTasks(username);
    }


    @GetMapping("/user-info")
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String authorizationHeader){
        try {
            String token = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            User user = userService.getUser(username);
            if (user!=null){
                user.setPassword(null);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
