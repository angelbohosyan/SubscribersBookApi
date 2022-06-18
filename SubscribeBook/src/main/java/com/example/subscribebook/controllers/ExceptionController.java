package com.example.subscribebook.controllers;

import com.example.subscribebook.ApiError;
import com.example.subscribebook.exceptions.UserRepositoryExceptions.CreateUserException;
import com.example.subscribebook.exceptions.UserRepositoryExceptions.GetUserWithNameException;
import com.example.subscribebook.exceptions.UserSaltRepositoryExceptions.GetSaltByUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(GetUserWithNameException.class)
    public ResponseEntity<?> GetUserWithNameExceptionHandler() {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(GetSaltByUserException.class)
    public ResponseEntity<?> GetSaltByUserExceptionHandler() {
        return ResponseEntity.notFound().build();
    }
}
