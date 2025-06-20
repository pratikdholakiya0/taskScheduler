package org.example.taskschedulersystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.taskschedulersystem.enums.Role;
import org.example.taskschedulersystem.model.User;
import org.example.taskschedulersystem.repository.UserRepository;
import org.example.taskschedulersystem.service.UserDetailsServiceImpl;
import org.example.taskschedulersystem.service.UserService;
import org.example.taskschedulersystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository u;

    @PostMapping("/signup")
    public void saveUser(@RequestBody User user){
        user.getRoles().add(Role.USER);
        user.setCreated_at(new Date(System.currentTimeMillis()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwt = jwtUtil.generateToken(user.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Incorrect username or password", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}
