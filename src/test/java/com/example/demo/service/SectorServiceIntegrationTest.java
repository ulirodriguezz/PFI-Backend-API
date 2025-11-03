package com.example.demo.service;

import com.example.demo.BaseIntegrationTest;
import com.example.demo.dto.FullSectorDTO;
import com.example.demo.dto.SimpleSectorDTO;
import com.example.demo.model.Sector;
import com.example.demo.repository.SectorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Tests de Integración - SectorService")
class SectorServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private SectorService sectorService;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Test
    @DisplayName("Debe crear y persistir sector correctamente")
    void shouldCreateAndPersistSector() {
        // Given
        SimpleSectorDTO sectorDto = new SimpleSectorDTO();
        sectorDto.setName("Almacén Principal");
        sectorDto.setDescription("Sector principal de almacenamiento");

        // When
        SimpleSectorDTO savedSector = sectorService.createSector(sectorDto);

        // Then
        assertThat(savedSector).isNotNull();
        assertThat(savedSector.getId()).isNotNull();
        assertThat(savedSector.getName()).isEqualTo("Almacén Principal");

        // Verificación de persistencia
        entityManager.flush();
        entityManager.clear();

        Sector persistedSector = sectorRepository.findById(savedSector.getId()).orElseThrow();
        assertThat(persistedSector.getName()).isEqualTo("Almacén Principal");
        assertThat(persistedSector.getTenant()).isNotNull();
    }

    @Test
    @DisplayName("Debe obtener sector por ID desde BD")
    void shouldGetSectorById() {
        // Given
        Sector sector = new Sector();
        sector.setName("Test Sector");
        sector.setDescription("Test Description");
        sector.setTenant(testTenant);
        sector = sectorRepository.save(sector);
        Long sectorId = sector.getId();

        entityManager.flush();
        entityManager.clear();

        // When
        FullSectorDTO foundSector = sectorService.getSectorById(sectorId);

        // Then
        assertThat(foundSector).isNotNull();
        assertThat(foundSector.getId()).isEqualTo(sectorId);
        assertThat(foundSector.getName()).isEqualTo("Test Sector");
    }

    @Test
    @DisplayName("Debe obtener todos los sectores")
    void shouldGetAllSectors() {
        // Given
        Sector sector1 = new Sector();
        sector1.setName("Sector 1");
        sector1.setDescription("Description 1");
        sector1.setTenant(testTenant);
        sectorRepository.save(sector1);

        Sector sector2 = new Sector();
        sector2.setName("Sector 2");
        sector2.setDescription("Description 2");
        sector2.setTenant(testTenant);
        sectorRepository.save(sector2);

        entityManager.flush();

        // When
        List<SimpleSectorDTO> sectors = sectorService.getAllSectors();

        // Then
        assertThat(sectors).isNotEmpty();
        assertThat(sectors).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Debe actualizar sector y persistir cambios")
    void shouldUpdateSectorAndPersist() {
        // Given
        Sector sector = new Sector();
        sector.setName("Original Name");
        sector.setDescription("Original Description");
        sector.setTenant(testTenant);
        sector = sectorRepository.save(sector);
        Long sectorId = sector.getId();

        entityManager.flush();
        entityManager.clear();

        SimpleSectorDTO updateData = new SimpleSectorDTO();
        updateData.setName("Updated Name");
        updateData.setDescription("Updated Description");

        // When
        sectorService.patchSector(sectorId, updateData);

        entityManager.flush();
        entityManager.clear();

        // Then
        Sector updatedSector = sectorRepository.findById(sectorId).orElseThrow();
        assertThat(updatedSector.getName()).isEqualTo("Updated Name");
        assertThat(updatedSector.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    @DisplayName("Debe eliminar sector de la BD")
    void shouldDeleteSector() {
        // Given
        Sector sector = new Sector();
        sector.setName("To Delete");
        sector.setDescription("Will be deleted");
        sector.setTenant(testTenant);
        sector = sectorRepository.save(sector);
        Long sectorId = sector.getId();

        entityManager.flush();
        assertThat(sectorRepository.existsById(sectorId)).isTrue();

        // When
        sectorService.deleteSector(sectorId);

        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(sectorRepository.existsById(sectorId)).isFalse();
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando sector no existe")
    void shouldThrowExceptionWhenSectorNotFound() {
        // When & Then
        assertThatThrownBy(() -> sectorService.getSectorById(99999L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Debe buscar sectores por nombre")
    void shouldSearchSectorsByName() {
        // Given
        Sector sector1 = new Sector();
        sector1.setName("Almacén Central");
        sector1.setDescription("Central");
        sector1.setTenant(testTenant);
        sectorRepository.save(sector1);

        Sector sector2 = new Sector();
        sector2.setName("Almacén Norte");
        sector2.setDescription("Norte");
        sector2.setTenant(testTenant);
        sectorRepository.save(sector2);

        entityManager.flush();

        // When
        List<SimpleSectorDTO> results = sectorService.getAllSectorsByQuery("Almacén");

        // Then
        assertThat(results).isNotEmpty();
        assertThat(results).hasSizeGreaterThanOrEqualTo(2);
    }
}

