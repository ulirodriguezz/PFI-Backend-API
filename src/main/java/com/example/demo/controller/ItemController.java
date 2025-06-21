package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
    @GetMapping("/items")
    public ResponseEntity<String> getAllItems(){
        return ResponseEntity.ok("Bien ahi papa");
    }
}
