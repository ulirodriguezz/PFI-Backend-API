package com.example.demo;

import com.example.demo.config.TestConfig;
import com.example.demo.helpers.TenantContext;
import com.example.demo.model.Tenant;
import com.example.demo.repository.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * Clase base para tests de integración.
 * Configura el contexto de Spring Boot con perfil de test y H2 database.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import(TestConfig.class)
public abstract class BaseIntegrationTest {

    @Autowired
    protected TenantRepository tenantRepository;

    protected Tenant testTenant;

    /**
     * Configuración inicial antes de cada test.
     * Crea un tenant de prueba y lo establece en el contexto.
     */
    @BeforeEach
    public void setUp() {
        // Crear tenant de prueba
        testTenant = new Tenant();
        testTenant.setName("Test Tenant");
        testTenant = tenantRepository.save(testTenant);
        
        // Establecer tenant en el contexto
        TenantContext.setTenantId(testTenant.getId());
    }
}

