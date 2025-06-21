package com.example.demo.mapper;

import com.example.demo.dto.SimpleUserDTO;
import com.example.demo.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toUserEntity(SimpleUserDTO dto){
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        return user;
    }

    public SimpleUserDTO toSimpleUserDTO(User user){
        SimpleUserDTO dto = new SimpleUserDTO.Builder()
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .build();
        return dto;
    }
}
