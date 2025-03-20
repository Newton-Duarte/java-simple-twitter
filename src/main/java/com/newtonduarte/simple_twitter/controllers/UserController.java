package com.newtonduarte.simple_twitter.controllers;

import com.newtonduarte.simple_twitter.entities.User;
import com.newtonduarte.simple_twitter.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/users")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<List<User>> getUsers() {
        var users = userRepository.findAll();

        return ResponseEntity.ok(users);
    }
}
