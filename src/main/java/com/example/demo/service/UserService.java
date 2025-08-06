package com.example.demo.service;


import com.example.demo.dto.*;
import com.example.demo.mapper.ItemMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Item;
import com.example.demo.model.User;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private PasswordEncoder passwordEncoder;
    private ItemService itemService;
    private ItemMapper itemMapper;

    private UserMapper userMapper;

    public UserService(UserRepository userRepository, ItemRepository itemRepository, PasswordEncoder passwordEncoder, ItemService itemService, ItemMapper itemMapper, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.passwordEncoder = passwordEncoder;
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        this.userMapper = userMapper;
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

    public UserProfileDTO getUserInfoByUsername(String username){
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        return userMapper.toUserProfileDTO(user);
    }

    public SimpleUserDTO getUserById(long id){
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toSimpleUserDTO(user);

    }

    public Set<SimpleItemDTO> getUserFavoriteItems(String userId){
        Set<Item> itemList = userRepository.getUserFavoriteItems(userId);
        return new HashSet<>(itemMapper.toSimpleItemDtoList(new ArrayList<>(itemList)));
    }

    @Transactional
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userRepository.save(user);
        return registeredUser;
    }

    private boolean validCredentials(String password,String dbPassword){
        return password.contentEquals(dbPassword);
    }
    @Transactional
    public void addItemToFavorites(String loggedUsername, ItemFavoritePostDTO itemData) {
        Item item = itemRepository.getItemById(itemData.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el item"));
        User user = userRepository.findUserByUsername(loggedUsername)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario"));
        user.getFavoriteItems().add(item);
        userRepository.save(user);
    }
    public void removeItemFromFavorites (String loggedUsername, long itemId){
        Item item = itemRepository.getItemById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el item"));
        User user = userRepository.findUserByUsername(loggedUsername)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro al usuario"));

        user.getFavoriteItems().remove(item);
        userRepository.save(user);
    }


}
