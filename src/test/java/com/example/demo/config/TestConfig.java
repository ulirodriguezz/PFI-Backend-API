package com.example.demo.config;

import com.example.demo.service.EmailService;
import com.example.demo.service.FirebaseStorageService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

/**
 * Configuración específica para tests.
 * Proporciona beans mock para servicios externos como Firebase y Email
 * para evitar problemas de conexión durante los tests.
 */
@TestConfiguration
@Profile("test")
public class TestConfig {

    /**
     * Bean mock de EmailService para evitar envíos reales de email en tests
     */
    @Bean
    @Primary
    public EmailService emailService() {
        return mock(EmailService.class);
    }

    /**
     * Bean mock de FirebaseStorageService para evitar conexiones a Firebase en tests
     */
    @Bean
    @Primary
    public FirebaseStorageService firebaseStorageService() {
        return mock(FirebaseStorageService.class);
    }
}

