package com.project.ordermakanan.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ordermakanan.dto.request.RegisterRequest;
import com.project.ordermakanan.dto.response.RegisterResponse;
import com.project.ordermakanan.models.User;
import com.project.ordermakanan.repositories.UserRepository;
import com.project.ordermakanan.services.UserService;

@RestController
@RequestMapping("/user-management")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @PostMapping("/user/sign-up")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }
}
