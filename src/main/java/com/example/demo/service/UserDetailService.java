package com.example.demo.service;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    public UserDetailService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username){
        User user = getUserByUsername(username);
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
        return userDetails;
    }
    private User getUserByUsername (String username){
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Credenciales invalidas (username)"));
        return user;
    }
    @PostConstruct
    public void testConfig() {
        System.out.println("SecurityConfig OK");
    }
}
