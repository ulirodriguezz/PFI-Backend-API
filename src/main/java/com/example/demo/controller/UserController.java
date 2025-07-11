package com.example.demo.controller;

import com.example.demo.dto.SimpleItemDTO;
import com.example.demo.dto.SimpleUserDTO;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/users/{userId}/favorites")
    public ResponseEntity<Set<SimpleItemDTO>>getAllFavoriteItems(@PathVariable long userId){
        Set<SimpleItemDTO> itemList = userService.getUserFavoriteItems(userId);
        return ResponseEntity.ok(itemList);
    }
}
