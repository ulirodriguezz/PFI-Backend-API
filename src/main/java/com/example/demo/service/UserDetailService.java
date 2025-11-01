package com.example.demo.service;
import com.example.demo.config.CustomUserDetails;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    public UserDetailService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username){
        User user = getUserByUsername(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        UserDetails userDetails = new CustomUserDetails(user.getUsername(),user.getPassword(),user.getTenant().getId(),authorities);
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
