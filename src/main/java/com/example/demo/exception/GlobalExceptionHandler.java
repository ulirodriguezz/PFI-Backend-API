package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<String> hanldeBadCredentials(BadCredentialsException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales invalidas");

    }
    @ExceptionHandler
    public ResponseEntity<String> handleUsernameNotFound(UsernameNotFoundException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nombre de usuario incorrecto");

    }
}
