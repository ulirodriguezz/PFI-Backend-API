package com.example.demo.controller;

import com.example.demo.dto.ItemFavoritePostDTO;
import com.example.demo.dto.Message;
import com.example.demo.dto.SimpleItemDTO;
import com.example.demo.dto.SimpleUserDTO;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    private UserService userService;

    public UserController (UserService userService) {
        this.userService = userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/users/{userId}/favorites")
    public ResponseEntity<Set<SimpleItemDTO>>getAllFavoriteItems(@PathVariable long userId){
        Set<SimpleItemDTO> itemList = userService.getUserFavoriteItems(userId);
        return ResponseEntity.ok(itemList);
    }
    @PostMapping("/users/{userId}/favorites")
    public ResponseEntity<Message>addItemToFavorites(@PathVariable long userId, @RequestBody ItemFavoritePostDTO itemData){
        userService.addItemToFavorites(userId,itemData);
        return ResponseEntity.ok(new Message("Se agregó el item a favoritos"));
    }
}
