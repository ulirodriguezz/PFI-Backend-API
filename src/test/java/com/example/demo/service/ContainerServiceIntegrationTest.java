package com.example.demo.service;

import com.example.demo.BaseIntegrationTest;
import com.example.demo.dto.FullContainerDTO;
import com.example.demo.dto.SimpleContainerDTO;
import com.example.demo.model.Container;
import com.example.demo.model.Sector;
import com.example.demo.repository.ContainerRepository;
import com.example.demo.repository.SectorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Tests de Integración - ContainerService (con verificación de persistencia)")
class ContainerServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ContainerService containerService;

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    private Sector testSector;

    @BeforeEach
    void setUpTestData() {
        testSector = new Sector();
        testSector.setName("Test Sector");
        testSector.setDescription("Test Description");
        testSector.setTenant(testTenant);
        testSector = sectorRepository.save(testSector);
    }

    @Test
    @DisplayName("Debe guardar contenedor en la BD y persistir correctamente")
    void shouldSaveContainerAndPersist() {
        // Given
        SimpleContainerDTO containerDto = new SimpleContainerDTO();
        containerDto.setName("Test Container");
        containerDto.setDescription("Test Description");
        containerDto.setSectorId(testSector.getId());

        // When - Guardamos el contenedor
        SimpleContainerDTO savedContainer = containerService.save(containerDto);

        // Then - Verificamos que se guardó correctamente
        assertThat(savedContainer).isNotNull();
        assertThat(savedContainer.getId()).isNotNull();
        assertThat(savedContainer.getName()).isEqualTo("Test Container");

        // Forzamos flush y clear para asegurar que se escribió en BD
        entityManager.flush();
        entityManager.clear();

        // Verificación EXPLÍCITA: Consultamos directamente desde BD
        Container persistedContainer = containerRepository.findById(savedContainer.getId())
                .orElseThrow(() -> new AssertionError("El contenedor no se persistió en la BD"));

        assertThat(persistedContainer.getName()).isEqualTo("Test Container");
        assertThat(persistedContainer.getDescription()).isEqualTo("Test Description");
        assertThat(persistedContainer.getSector().getId()).isEqualTo(testSector.getId());
    }

    @Test
    @DisplayName("Debe actualizar contenedor y persistir cambios en BD")
    void shouldUpdateContainerAndPersistChanges() {
        // Given - Creamos un contenedor inicial
        Container container = new Container();
        container.setName("Original Name");
        container.setDescription("Original Description");
        container.setSector(testSector);
        container.setTenant(testTenant);
        container = containerRepository.save(container);
        Long containerId = container.getId();

        // Forzamos que se escriba en BD
        entityManager.flush();
        entityManager.clear();

        // When - Actualizamos el contenedor
        SimpleContainerDTO updateData = new SimpleContainerDTO();
        updateData.setName("Updated Name");
        updateData.setDescription("Updated Description");
        
        containerService.updateContainer(containerId, updateData);

        // Forzamos flush para asegurar que se escribió
        entityManager.flush();
        entityManager.clear();

        // Then - Verificamos que los cambios se persistieron
        Container updatedContainer = containerRepository.findById(containerId)
                .orElseThrow(() -> new AssertionError("El contenedor no existe en la BD"));

        assertThat(updatedContainer.getName()).isEqualTo("Updated Name");
        assertThat(updatedContainer.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    @DisplayName("Debe eliminar contenedor de la BD")
    void shouldDeleteContainerFromDatabase() {
        // Given
        Container container = new Container();
        container.setName("To Delete");
        container.setDescription("Will be deleted");
        container.setSector(testSector);
        container.setTenant(testTenant);
        container = containerRepository.save(container);
        Long containerId = container.getId();

        // Verificamos que existe
        entityManager.flush();
        assertThat(containerRepository.existsById(containerId)).isTrue();

        // When - Eliminamos el contenedor
        containerService.deleteById(containerId);

        // Forzamos flush y clear
        entityManager.flush();
        entityManager.clear();

        // Then - Verificamos que ya no existe en la BD
        assertThat(containerRepository.existsById(containerId)).isFalse();
    }

    @Test
    @DisplayName("Debe obtener contenedor por ID desde la BD")
    void shouldGetContainerByIdFromDatabase() {
        // Given - Guardamos un contenedor
        Container container = new Container();
        container.setName("Test Container");
        container.setDescription("Test Description");
        container.setSector(testSector);
        container.setTenant(testTenant);
        container = containerRepository.save(container);
        Long containerId = container.getId();

        // Limpiamos el contexto de persistencia
        entityManager.flush();
        entityManager.clear();

        // When - Obtenemos el contenedor (debe consultarlo desde BD)
        FullContainerDTO foundContainer = containerService.getContainerById(containerId);

        // Then
        assertThat(foundContainer).isNotNull();
        assertThat(foundContainer.getId()).isEqualTo(containerId);
        assertThat(foundContainer.getName()).isEqualTo("Test Container");
    }

//    @Test
//    @DisplayName("Debe obtener todos los contenedores de un sector desde BD")
//    void shouldGetAllContainersBySectorFromDatabase() {
//        // Given - Creamos varios contenedores
//        Container container1 = new Container();
//        container1.setName("Container 1");
//        container1.setDescription("Description 1");
//        container1.setSector(testSector);
//        container1.setTenant(testTenant);
//        containerRepository.save(container1);
//
//        Container container2 = new Container();
//        container2.setName("Container 2");
//        container2.setDescription("Description 2");
//        container2.setSector(testSector);
//        container2.setTenant(testTenant);
//        containerRepository.save(container2);
//
//        // Forzamos persistencia
//        entityManager.flush();
//        entityManager.clear();
//
//        // When - Obtenemos todos los contenedores del sector
//        List<SimpleContainerDTO> containers = containerService.geCon(testSector.getId());
//
//        // Then - Verificamos que se recuperaron desde BD
//        assertThat(containers).isNotEmpty();
//        assertThat(containers).hasSize(2);
//        assertThat(containers).extracting("name")
//                .containsExactlyInAnyOrder("Container 1", "Container 2");
//    }

    @Test
    @DisplayName("Debe lanzar excepción cuando contenedor no existe en BD")
    void shouldThrowExceptionWhenContainerNotInDatabase() {
        // Given - ID que no existe en BD
        Long nonExistentId = 99999L;

        // When & Then
        assertThatThrownBy(() -> containerService.getContainerById(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("No se encontro el contenedor");
    }

    @Test
    @DisplayName("Debe manejar correctamente relaciones al guardar")
    void shouldHandleRelationshipsWhenSaving() {
        // Given
        SimpleContainerDTO containerDto = new SimpleContainerDTO();
        containerDto.setName("Container with Relationship");
        containerDto.setDescription("Has sector relationship");
        containerDto.setSectorId(testSector.getId());

        // When
        SimpleContainerDTO savedContainer = containerService.save(containerDto);

        // Forzamos persistencia y limpiamos cache
        entityManager.flush();
        entityManager.clear();

        // Then - Verificamos que la relación se guardó correctamente
        Container persistedContainer = containerRepository.findById(savedContainer.getId()).orElseThrow();
        
        assertThat(persistedContainer.getSector()).isNotNull();
        assertThat(persistedContainer.getSector().getId()).isEqualTo(testSector.getId());
        assertThat(persistedContainer.getSector().getName()).isEqualTo("Test Sector");
    }
}

