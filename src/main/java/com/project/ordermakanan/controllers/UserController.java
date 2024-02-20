package com.project.ordermakanan.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ordermakanan.dto.request.LoginRequest;
import com.project.ordermakanan.dto.request.RegisterRequest;
import com.project.ordermakanan.dto.response.LoginResponse;
import com.project.ordermakanan.dto.response.MessageResponse;
import com.project.ordermakanan.models.User;
import com.project.ordermakanan.models.UserDetail;
import com.project.ordermakanan.repositories.UserRepository;
import com.project.ordermakanan.services.UserService;
import com.project.ordermakanan.utils.jwt.JwtUtil;

@RestController
@RequestMapping("/user-management")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/user/data")
    public ResponseEntity<MessageResponse> getAllUsers() {
        UserDetail userDetail = JwtUtil.getCurrentUser();
        int userId = userDetail.getUserId();
        String username = userDetail.getUsername();
        return ResponseEntity.ok().body(new MessageResponse("User ID: " + userId + ", Username: " + username, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
    }
    
    @PostMapping("/users/sign-up")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/users/sign-in")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return userService.loginUser(request);
    }
}
