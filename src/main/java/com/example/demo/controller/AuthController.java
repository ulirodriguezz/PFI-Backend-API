package com.example.demo.controller;

import com.example.demo.config.JwtProvider;
import com.example.demo.dto.Message;
import com.example.demo.dto.PasswordChangeRequestDTO;
import com.example.demo.dto.SimpleUserDTO;
import com.example.demo.dto.UserCredentials;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/auth/validate-token/{userId}")
    public ResponseEntity<SimpleUserDTO> loginWithToken (@RequestHeader("Authorization") String authHeader,@PathVariable long userId){

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("token invalido");
        }
        String token = authHeader.replace("Bearer ","");
        if(!jwtProvider.validateToken(token))
            throw new BadCredentialsException("Token invalido");

        SimpleUserDTO loggedUser = userService.getUserById(userId);
        loggedUser.setToken(token);
        return ResponseEntity.ok(loggedUser);
    }

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<Message> passwordChange (@RequestBody PasswordChangeRequestDTO dto){
        userService.resetPassword(dto.email());
        return ResponseEntity.ok(new Message("Mail enviado"));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Up and running");
    }

}
