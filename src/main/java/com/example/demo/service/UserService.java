package com.example.demo.service;

import com.example.demo.dto.SimpleUserDTO;
import com.example.demo.dto.UserCredentials;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }
    public User getUsernameByCredentials(UserCredentials credentials){
        User storedUser = this.getUserByUsername(credentials.getUsername());
        if(!validCredentials(credentials.getPassword(),storedUser.getPassword())){
            throw new BadCredentialsException("Contraseña incorrecta");
        }
        return storedUser;
    }

    public User getUserByUsername (String username){
            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new BadCredentialsException("Credenciales invalidas (username)"));
            return user;
    }


    private boolean validCredentials(String password,String dbPassword){
        return password.contentEquals(dbPassword);
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userRepository.save(user);
        return registeredUser;
    }
}
