package com.example.demo.controller;

import com.example.demo.config.JwtProvider;
import com.example.demo.dto.*;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    public UserController (UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/user/favorites")
    public ResponseEntity<Set<SimpleItemDTO>>getAllFavoriteItems(@RequestHeader("Authorization") String authToken){
        String token = jwtProvider.getTokenFromHeader(authToken);
        String loggedUsername = jwtProvider.getUsernameFromToken(token);
        Set<SimpleItemDTO> itemList = userService.getUserFavoriteItems(loggedUsername);
        return ResponseEntity.ok(itemList);
    }
    @PostMapping("/user/favorites")
    public ResponseEntity<Message>addItemToFavorites(@RequestHeader("Authorization") String authToken, @RequestBody ItemFavoritePostDTO itemData){
        String token = jwtProvider.getTokenFromHeader(authToken);
        String loggedUsername = jwtProvider.getUsernameFromToken(token);
        userService.addItemToFavorites(loggedUsername,itemData);
        return ResponseEntity.ok(new Message("Se agregó el item a favoritos"));
    }
    @DeleteMapping ("/user/favorites/{itemId}")
    public ResponseEntity<Message>removeItemFromFavorites(@RequestHeader("Authorization") String authToken, @PathVariable long itemId){
        String token = jwtProvider.getTokenFromHeader(authToken);
        String loggedUsername = jwtProvider.getUsernameFromToken(token);
        userService.removeItemFromFavorites(loggedUsername,itemId);
        return ResponseEntity.ok(new Message("Se elimino el item de favoritos"));
    }
    @GetMapping("/user/profile")
    public ResponseEntity<UserProfileDTO> getLoggedUserProfile(@RequestHeader("Authorization") String authHeader){
        String token = jwtProvider.getTokenFromHeader(authHeader);
        String loggedUserName = jwtProvider.getUsernameFromToken(token);

        UserProfileDTO user = userService.getUserInfoByUsername(loggedUserName);

        return ResponseEntity.ok(user);
    }
}
