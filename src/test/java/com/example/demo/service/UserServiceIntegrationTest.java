package com.example.demo.service;

import com.example.demo.BaseIntegrationTest;
import com.example.demo.dto.*;
import com.example.demo.model.Item;
import com.example.demo.model.User;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.types.UserRoleType;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@DisplayName("Tests de Integración - UserService")
class UserServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private User testUser;
    private User testAdmin;

    @BeforeEach
    void setUpUsers() {
        // Mock del servicio de email para evitar envíos reales
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        // Crear usuario de prueba
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setName("Test");
        testUser.setSurname("User");
        testUser.setEmail("test@example.com");
        testUser.setRole(UserRoleType.USER);
        testUser.setDisabled(false);
        testUser.setTenant(testTenant);
        testUser = userRepository.save(testUser);

        // Crear admin de prueba
        testAdmin = new User();
        testAdmin.setUsername("admin");
        testAdmin.setPassword(passwordEncoder.encode("admin123"));
        testAdmin.setName("Admin");
        testAdmin.setSurname("User");
        testAdmin.setEmail("admin@example.com");
        testAdmin.setRole(UserRoleType.ADMIN);
        testAdmin.setDisabled(false);
        testAdmin.setTenant(testTenant);
        testAdmin = userRepository.save(testAdmin);
    }

    @Test
    @DisplayName("Debe obtener todos los usuarios activos")
    void shouldGetAllActiveUsers() {
        // When
        List<UserProfileDTO> users = userService.getAllUsers();

        // Then
        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(2);
        assertThat(users).extracting("username")
                .contains("testuser", "admin");
    }

    @Test
    @DisplayName("Debe obtener usuario por username")
    void shouldGetUserByUsername() {
        // When
        User user = userService.getUserByUsername("testuser");

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testuser");
        assertThat(user.getName()).isEqualTo("Test");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando usuario no existe")
    void shouldThrowExceptionWhenUserNotFound() {
        // When & Then
        assertThatThrownBy(() -> userService.getUserByUsername("noexiste"))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Credenciales invalidas");
    }

    @Test
    @DisplayName("Debe obtener información de perfil de usuario")
    void shouldGetUserProfileInfo() {
        // When
        UserProfileDTO profile = userService.getUserInfoByUsername("testuser");

        // Then
        assertThat(profile).isNotNull();
        assertThat(profile.getUsername()).isEqualTo("testuser");
        assertThat(profile.getName()).isEqualTo("Test");
        assertThat(profile.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Debe obtener usuario por ID")
    void shouldGetUserById() {
        // When
        SimpleUserDTO user = userService.getUserById(testUser.getId());

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Debe registrar un nuevo usuario")
    void shouldRegisterNewUser() {
        // Given
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("password123");
        newUser.setName("New");
        newUser.setSurname("User");
        newUser.setEmail("newuser@example.com");

        // When
        User registeredUser = userService.registerUser(newUser);

        // Then
        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getId()).isNotNull();
        assertThat(registeredUser.getUsername()).isEqualTo("newuser");
        assertThat(registeredUser.isDisabled()).isTrue(); // Los nuevos usuarios inician deshabilitados
        assertThat(registeredUser.getRole()).isEqualTo(UserRoleType.USER);
        
        // Verificar que la contraseña está encriptada
        assertThat(registeredUser.getPassword()).isNotEqualTo("password123");
    }

    @Test
    @DisplayName("Debe actualizar el perfil de usuario")
    void shouldUpdateUserProfile() {
        // Given
        UserProfileDTO updateData = new UserProfileDTO();
        updateData.setName("Updated");
        updateData.setSurname("Name");
        updateData.setEmail("updated@example.com");

        // When
        UserProfileDTO updatedProfile = userService.patchUser("testuser", updateData);

        // Then
        assertThat(updatedProfile).isNotNull();
        assertThat(updatedProfile.getName()).isEqualTo("Updated");
        assertThat(updatedProfile.getSurname()).isEqualTo("Name");
        assertThat(updatedProfile.getEmail()).isEqualTo("updated@example.com");
    }

//    @Test
//    @DisplayName("Debe actualizar contraseña de usuario")
//    void shouldUpdateUserPassword() {
//        // Given
//        PasswordUpdateDTO passwordUpdate = new PasswordUpdateDTO("newPassword123");
//
//        // When
//        UserProfileDTO updatedUser = userService.updatePassword("testuser", passwordUpdate);
//
//        // Then
//        assertThat(updatedUser).isNotNull();
//
//        // Verificar que la contraseña fue actualizada
//        User user = userRepository.findUserByUsername("testuser").orElseThrow();
//        assertThat(passwordEncoder.matches("newPassword123", user.getPassword())).isTrue();
//    }

    @Test
    @DisplayName("Debe agregar item a favoritos")
    void shouldAddItemToFavorites() {
        // Given
        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setTenant(testTenant);
        item = itemRepository.save(item);

        ItemFavoritePostDTO favoriteData = new ItemFavoritePostDTO();
        favoriteData.setItemId(item.getId());

        // When
        userService.addItemToFavorites("testuser", favoriteData);

        // Then
        Set<SimpleItemDTO> favorites = userService.getUserFavoriteItems("testuser");
        assertThat(favorites).isNotEmpty();
        assertThat(favorites).hasSize(1);
    }

    @Test
    @DisplayName("Debe remover item de favoritos")
    void shouldRemoveItemFromFavorites() {
        // Given
        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setTenant(testTenant);
        item = itemRepository.save(item);

        // Agregar a favoritos primero
        ItemFavoritePostDTO favoriteData = new ItemFavoritePostDTO();
        favoriteData.setItemId(item.getId());
        userService.addItemToFavorites("testuser", favoriteData);

        // When
        userService.removeItemFromFavorites("testuser", item.getId());

        // Then
        Set<SimpleItemDTO> favorites = userService.getUserFavoriteItems("testuser");
        assertThat(favorites).isEmpty();
    }

    @Test
    @DisplayName("Debe resetear contraseña de usuario")
    void shouldResetPassword() {
        // When
        userService.resetPassword("test@example.com");

        // Then
        User user = userRepository.findUserByEmail("test@example.com").orElseThrow();
        assertThat(user.getPassword()).isNotEqualTo(passwordEncoder.encode("password123"));
    }

    @Test
    @DisplayName("Debe lanzar excepción al resetear contraseña de email inexistente")
    void shouldThrowExceptionWhenResettingPasswordForNonExistentEmail() {
        // When & Then
        assertThatThrownBy(() -> userService.resetPassword("noexiste@example.com"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("No se encontró el usuario");
    }

    @Test
    @DisplayName("Debe activar usuario deshabilitado")
    void shouldEnableDisabledUser() {
        // Given
        User disabledUser = new User();
        disabledUser.setUsername("disabled");
        disabledUser.setPassword(passwordEncoder.encode("pass"));
        disabledUser.setName("Disabled");
        disabledUser.setSurname("User");
        disabledUser.setEmail("disabled@example.com");
        disabledUser.setDisabled(true);
        disabledUser.setTenant(testTenant);
        disabledUser = userRepository.save(disabledUser);

        // When
        userService.updateUserStatus("disabled", false);

        // Then
        User updatedUser = userRepository.findUserByUsername("disabled").orElseThrow();
        assertThat(updatedUser.isDisabled()).isFalse();
    }

    @Test
    @DisplayName("Debe obtener usuarios deshabilitados")
    void shouldGetDisabledUsers() {
        // Given
        User disabledUser = new User();
        disabledUser.setUsername("disabled");
        disabledUser.setPassword(passwordEncoder.encode("pass"));
        disabledUser.setName("Disabled");
        disabledUser.setSurname("User");
        disabledUser.setEmail("disabled@example.com");
        disabledUser.setDisabled(true);
        disabledUser.setTenant(testTenant);
        userRepository.save(disabledUser);

        // When
        List<UserProfileDTO> disabledUsers = userService.getAllDisabledUsers();

        // Then
        assertThat(disabledUsers).isNotEmpty();
        assertThat(disabledUsers).hasSize(1);
        assertThat(disabledUsers.get(0).getUsername()).isEqualTo("disabled");
    }
}

