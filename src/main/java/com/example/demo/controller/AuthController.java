package com.example.demo.controller;

import com.example.demo.config.JwtProvider;
import com.example.demo.dto.SimpleUserDTO;
import com.example.demo.dto.UserCredentials;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    public AuthController(UserService userService, UserMapper userMapper,
                          AuthenticationManager authenticationManager,
                          JwtProvider jwtProvider,PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserCredentials credentials){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),credentials.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtProvider.generateToken(auth);
        User user = userService.getUserByUsername(credentials.getUsername());
        SimpleUserDTO loggedUserDTO = userMapper.toSimpleUserDTO(user);
        loggedUserDTO.setToken(token);
        return ResponseEntity.ok(loggedUserDTO);
    }
    @PostMapping("/register")
    public ResponseEntity<SimpleUserDTO> register (@RequestBody SimpleUserDTO newUser){
        User registeredUser = userService.registerUser(userMapper.toUserEntity(newUser));
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toSimpleUserDTO(registeredUser));
    }

}
