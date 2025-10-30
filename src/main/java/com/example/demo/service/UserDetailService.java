package com.example.demo.service;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


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
                .authorities(user.getRole().toString())
                .build();
        return userDetails;
    }
    private User getUserByUsername (String username){
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Credenciales invalidas"));
        if(user.isDisabled())
            throw new BadCredentialsException("Usuario no habilitado");
        return user;
    }

}
