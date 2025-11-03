package com.example.demo.service;

import com.example.demo.BaseIntegrationTest;
import com.example.demo.dto.MovementGetDTO;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests de Integración - MovementService")
class MovementServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MovementService movementService;

    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EmailService emailService;

    private Item testItem;
    private Container sourceContainer;
    private Container destinationContainer;
    private Sector testSector;

    @BeforeEach
    void setUpTestData() {
        // Crear sector
        testSector = new Sector();
        testSector.setName("Test Sector");
        testSector.setDescription("Test Description");
        testSector.setTenant(testTenant);
        testSector = sectorRepository.save(testSector);

        // Crear contenedor origen
        sourceContainer = new Container();
        sourceContainer.setName("Source Container");
        sourceContainer.setDescription("Source");
        sourceContainer.setSector(testSector);
        sourceContainer.setTenant(testTenant);
        sourceContainer = containerRepository.save(sourceContainer);

        // Crear contenedor destino
        destinationContainer = new Container();
        destinationContainer.setName("Destination Container");
        destinationContainer.setDescription("Destination");
        destinationContainer.setSector(testSector);
        destinationContainer.setTenant(testTenant);
        destinationContainer = containerRepository.save(destinationContainer);

        // Crear item
        testItem = new Item();
        testItem.setName("Test Item");
        testItem.setDescription("Test Description");
        testItem.setContainer(sourceContainer);
        testItem.setTenant(testTenant);
        testItem = itemRepository.save(testItem);
    }

    @Test
    @DisplayName("Debe registrar movimiento y persistir en BD")
    void shouldRegisterMovementAndPersist() {
        // Given
        int initialMovementCount = movementRepository.findAll().size();

        // When
        movementService.registerNewMovement(testItem, destinationContainer);

        // Forzar persistencia
        entityManager.flush();
        entityManager.clear();

        // Then
        List<Movement> allMovements = movementRepository.findAll();
        assertThat(allMovements).hasSize(initialMovementCount + 1);

        Movement persistedMovement = allMovements.get(allMovements.size() - 1);
        assertThat(persistedMovement.getItem().getId()).isEqualTo(testItem.getId());
        assertThat(persistedMovement.getDestinationContainer().getId()).isEqualTo(destinationContainer.getId());
        assertThat(persistedMovement.getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Debe obtener historial de movimientos de un item")
    void shouldGetItemMovementHistory() {
        // Given - Registrar varios movimientos
        movementService.registerNewMovement(testItem, destinationContainer);
        
        // Mover de nuevo
        testItem.setContainer(destinationContainer);
        testItem = itemRepository.save(testItem);
        movementService.registerNewMovement(testItem, sourceContainer);

        entityManager.flush();

        // When
        List<MovementGetDTO> movements = movementService.getAllMovementsByItemId(testItem.getId());

        // Then
        assertThat(movements).isNotEmpty();
        assertThat(movements).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Debe eliminar todos los movimientos de un item")
    void shouldDeleteAllMovementsByItemId() {
        // Given
        movementService.registerNewMovement(testItem, destinationContainer);
        movementService.registerNewMovement(testItem, sourceContainer);
        
        entityManager.flush();
        entityManager.clear();

        List<MovementGetDTO> beforeDelete = movementService.getAllMovementsByItemId(testItem.getId());
        assertThat(beforeDelete).isNotEmpty();

        // When
        movementService.deleteAllMovementsByItemId(testItem.getId());

        entityManager.flush();
        entityManager.clear();

        // Then
        List<MovementGetDTO> afterDelete = movementService.getAllMovementsByItemId(testItem.getId());
        assertThat(afterDelete).isEmpty();
    }

    @Test
    @DisplayName("Debe persistir timestamp al crear movimiento")
    void shouldPersistTimestampWhenCreatingMovement() {
        // When
        movementService.registerNewMovement(testItem, destinationContainer);

        entityManager.flush();
        entityManager.clear();

        // Then
        List<Movement> movements = movementRepository.findAll();
        assertThat(movements).isNotEmpty();
        
        Movement movement = movements.get(movements.size() - 1);
        assertThat(movement.getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Debe mantener relaciones correctas al registrar movimiento")
    void shouldMaintainRelationshipsWhenRegisteringMovement() {
        // When
        movementService.registerNewMovement(testItem, destinationContainer);

        entityManager.flush();
        entityManager.clear();

        // Then - Verificar que las relaciones se guardaron correctamente
        List<Movement> movements = movementRepository.findAll();
        Movement movement = movements.get(movements.size() - 1);

        assertThat(movement.getItem()).isNotNull();
        assertThat(movement.getDestinationContainer()).isNotNull();
        
        assertThat(movement.getItem().getName()).isEqualTo("Test Item");
        assertThat(movement.getDestinationContainer().getName()).isEqualTo("Destination Container");
    }
}

