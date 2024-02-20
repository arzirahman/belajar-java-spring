package com.project.ordermakanan.services;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.ordermakanan.dto.request.RegisterRequest;
import com.project.ordermakanan.dto.response.RegisterResponse;
import com.project.ordermakanan.models.User;
import com.project.ordermakanan.repositories.UserRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class UserService {

    @Autowired
    private Validator validator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Transactional
    public ResponseEntity<RegisterResponse> registerUser(RegisterRequest request){
        Set<ConstraintViolation<RegisterRequest>> constraintViolations = validator.validate(request);

        if(!constraintViolations.isEmpty()){
            ConstraintViolation<RegisterRequest> firstViolation = constraintViolations.iterator().next();
            String errorMessage = firstViolation.getMessage();
            return ResponseEntity
                .badRequest()
                .body(
                    RegisterResponse
                        .builder()
                        .message(errorMessage)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .build()
                );
        }

        if(!request.getPassword().equals(request.getRetypePassword())){
            String message = messageSource.getMessage("retype.password.mismatch", null, Locale.getDefault());
            return ResponseEntity
                .badRequest()
                .body(
                    RegisterResponse
                        .builder()
                        .message(message)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .build()
                );
        }

        if(userRepository.existsByUsername(request.getUsername())){
            String message = messageSource.getMessage("username.exists", null, Locale.getDefault());
            return ResponseEntity
                .badRequest()
                .body(
                    RegisterResponse
                        .builder()
                        .message(message)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .build()
                );
        }


        User user = User.builder()
            .username(request.getUsername())
            .fullname(request.getFullname())
            .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
            .isDeleted(false)
            .build();

        userRepository.save(user);
        
        String message = messageSource.getMessage("register.successful", null, Locale.getDefault());
        String formatMessage = MessageFormat.format(message, request.getUsername());

        return ResponseEntity
            .ok()
            .body(
                RegisterResponse
                    .builder()
                    .message(formatMessage)
                    .statusCode(HttpStatus.OK.value())
                    .status(HttpStatus.OK.getReasonPhrase())
                    .build()
                );
    }
}
