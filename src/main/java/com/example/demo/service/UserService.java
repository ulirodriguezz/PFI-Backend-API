package com.example.demo.service;


import com.example.demo.dto.*;
import com.example.demo.helpers.PasswordGenerator;
import com.example.demo.mapper.ItemMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Item;
import com.example.demo.model.Tenant;
import com.example.demo.model.User;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.TenantRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;
    private final TenantRepository tenantRepository;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    private final EmailService emailService;

    public UserService(UserRepository userRepository, ItemRepository itemRepository, PasswordEncoder passwordEncoder, TenantRepository tenantRepository, ItemMapper itemMapper, UserMapper userMapper, EmailService emailService) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.passwordEncoder = passwordEncoder;
        this.tenantRepository = tenantRepository;
        this.itemMapper = itemMapper;
        this.userMapper = userMapper;
        this.emailService = emailService;
    }

    public List<UserProfileDTO> getAllUsers() {
        List<User> storedUsers = this.userRepository.findAllByIsDisabled(false);
        return userMapper.toUserProfileDTOList(storedUsers);
    }

    public List<UserProfileDTO> getAllDisabledUsers() {
        List<User> storedUsers = this.userRepository.findAllByIsDisabled(true);
        return userMapper.toUserProfileDTOList(storedUsers);
    }

    public User getUsernameByCredentials(UserCredentials credentials) {
        User storedUser = this.getUserByUsername(credentials.getUsername());
        if (!validCredentials(credentials.getPassword(), storedUser.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }
        return storedUser;
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Credenciales invalidas (username)"));
        return user;
    }

    public UserProfileDTO getUserInfoByUsername(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        return userMapper.toUserProfileDTO(user);
    }

    public SimpleUserDTO getUserById(long id) {
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toSimpleUserDTO(user);

    }

    public Set<SimpleItemDTO> getUserFavoriteItems(String userId) {
        Set<Item> itemList = userRepository.getUserFavoriteItems(userId);
        return new HashSet<>(itemMapper.toSimpleItemDtoList(new ArrayList<>(itemList)));
    }

    @Transactional
    public UserProfileDTO updateUserStatus(String username, boolean isDisabled) {
        User storedUser = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("No se encontrò el usuario"));

        if (isDisabled) {
            emailService.sendEmail(storedUser.getEmail(), "Registro de usuario FindIT",

                    "Hola " + storedUser.getName() + ",\n \n" +
                            "Tu solicitud de registro en FindIT fue rechazada por el administrador." + "\n \n" +
                            "Para acceder a FindIT, tu solicitud de registro de usuario debe ser aceptada por el administrador" + "\n \n" +
                            "Asegurate de hablar con el antes de intentar un nuevo registro" + "\n \n" +
                            "Saludos cordiales," + "\n" +
                            "El equipo de FindIT");
            userRepository.delete(storedUser);
        }

        storedUser.setDisabled(isDisabled);

        User savedUser = userRepository.save(storedUser);

        emailService.sendEmail(savedUser.getEmail(), "Registro de usuario FindIT",

                "Hola " + savedUser.getName() + ",\n \n" +
                        "Tu solicitud de registro en FindIT fue aceptada!" + "\n \n" +
                        "Pordrás ingresar a la aplicacion con tu nombre de usuario: " + savedUser.getUsername() +" y tu contraseña" +"\n \n" +
                        "Te damos la bienvenida a FindIT, esperamos que disfrutes de la aplicación!" + "\n \n" +
                        "Saludos cordiales," + "\n" +
                        "El equipo de FindIT");

        return userMapper.toUserProfileDTO(savedUser);
    }

    @Transactional
    public User registerUser(User user) {
        Tenant tenant = tenantRepository.findById(1L)
                .orElseGet(() -> {
                    Tenant uadeTenant = new Tenant();
                    uadeTenant.setName("UADE");
                    return tenantRepository.save(uadeTenant);
                });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setTenant(tenant);
        user.setDisabled(true);

        User registeredUser = userRepository.save(user);

        emailService.sendEmail(registeredUser.getEmail(), "Registro de usuario FindIT",
                "Hola " + user.getName() + ",\n \n" +
                        "Gracias por registrarte en FindIT!" + "\n \n" +
                        "Para completar tu registro, un administrador de " + tenant.getName() + " revisará tu solicitud. \n \n" +
                        "Una vez que seas aceptado, recibiras un correo con una contraseña para ingresar a FindIT con tu nombre de usuario" + "\n \n" +
                        "Al ingresar a la aplicación, podrás cambiar tu contraseña cuando quieras desde la seccion Mi perfil -> cambio de contraseña" + "\n \n" +
                        "Esperamos que pronto puedas disfrutar de la comodidad de FindIT" + "\n \n" +
                        "Saludos cordiales," + "\n" +
                        "El equipo de FindIT"
        );

        return registeredUser;
    }

    private boolean validCredentials(String password, String dbPassword) {
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

    public void removeItemFromFavorites(String loggedUsername, long itemId) {
        Item item = itemRepository.getItemById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el item"));
        User user = userRepository.findUserByUsername(loggedUsername)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro al usuario"));

        user.getFavoriteItems().remove(item);
        userRepository.save(user);
    }


    public UserProfileDTO patchUser(String loggedUserName, UserProfileDTO updateData) {
        User dbUser = userRepository.findUserByUsername(loggedUserName)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        userMapper.mergeUpdates(updateData, dbUser);

        User updatedStoredUser = userRepository.save(dbUser);

        return userMapper.toUserProfileDTO(updatedStoredUser);
    }

    public void resetPassword(String email) {
        User storedUser = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario"));

        String generatedPassword = PasswordGenerator.generatePassword(12);
        storedUser.setPassword(passwordEncoder.encode(generatedPassword));

        storedUser = userRepository.save(storedUser);

        emailService.sendEmail(storedUser.getEmail(),"Olvido de contraseña FindIT",
                "Hola " + storedUser.getName() + ",\n \n" +
                        "Solicitaste un mail de recuperación de contraseña para tu usuario de FindIT" + "\n \n" +
                        "Tu nueva contraseña es: " +generatedPassword+ "\n \n" +
                        "Te recomendamos que, al ingresar a la aplicación, cambies tu contraseña desde seccion Mi perfil -> cambio de contraseña" + "\n \n" +
                        "IMPORTANTE: No compartas tu nueva contraseña con nadie. Si no solicitaste este email, ponete en contacto con el administrador de tu compañia o contactate con el soporte de FindIT (finditsupport@gmail.com)" + "\n \n" +
                        "Saludos cordiales," + "\n" +
                        "El equipo de FindIT"
        );
    }
}
