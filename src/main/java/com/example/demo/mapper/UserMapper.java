package com.example.demo.mapper;

import com.example.demo.dto.SimpleUserDTO;
import com.example.demo.dto.UserProfileDTO;
import com.example.demo.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toUserEntity(SimpleUserDTO dto){
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPassword(dto.getPassword());
        return user;
    }

    public SimpleUserDTO toSimpleUserDTO(User user){
        SimpleUserDTO dto = new SimpleUserDTO.Builder()
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .id(user.getId())
                .build();
        return dto;
    }

    public UserProfileDTO toUserProfileDTO (User user){
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
