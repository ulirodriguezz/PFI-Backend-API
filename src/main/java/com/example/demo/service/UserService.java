package com.example.demo.service;


import com.example.demo.dto.ItemFavoritePostDTO;
import com.example.demo.dto.SimpleItemDTO;
import com.example.demo.dto.UserCredentials;
import com.example.demo.mapper.ItemMapper;
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

    public UserService(UserRepository userRepository, ItemRepository itemRepository, PasswordEncoder passwordEncoder, ItemService itemService, ItemMapper itemMapper) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.passwordEncoder = passwordEncoder;
        this.itemService = itemService;
        this.itemMapper = itemMapper;
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

    public Set<SimpleItemDTO> getUserFavoriteItems(long userId){
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

    public void addItemToFavorites(long userId, ItemFavoritePostDTO itemData) {
        Item item = itemRepository.getItemById(itemData.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el item"));
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario"));
        user.getFavoriteItems().add(item);
        userRepository.save(user);
    }
}
