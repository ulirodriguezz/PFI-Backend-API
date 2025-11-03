package com.example.demo.service;

import com.example.demo.BaseIntegrationTest;
import com.example.demo.dto.FullItemDTO;
import com.example.demo.dto.SimpleItemDTO;
import com.example.demo.model.Container;
import com.example.demo.model.Item;
import com.example.demo.model.Sector;
import com.example.demo.model.User;
import com.example.demo.repository.ContainerRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.SectorRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.types.UserRoleType;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Tests de Integración - ItemService")
class ItemServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    private Container testContainer;
    private User testUser;
    private Sector testSector;

    @BeforeEach
    void setUpTestData() {
        // Crear sector de prueba
        testSector = new Sector();
        testSector.setName("Test Sector");
        testSector.setDescription("Test Description");
        testSector.setTenant(testTenant);
        testSector = sectorRepository.save(testSector);

        // Crear contenedor de prueba
        testContainer = new Container();
        testContainer.setName("Test Container");
        testContainer.setDescription("Test Description");
        testContainer.setSector(testSector);
        testContainer.setTenant(testTenant);
        testContainer = containerRepository.save(testContainer);

        // Crear usuario de prueba
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setName("Test");
        testUser.setSurname("User");
        testUser.setEmail("test@example.com");
        testUser.setRole(UserRoleType.USER);
        testUser.setTenant(testTenant);
        testUser = userRepository.save(testUser);
    }

    @Test
    @DisplayName("Debe crear un nuevo item sin contenedor")
    void shouldCreateNewItemWithoutContainer() {
        // Given
        SimpleItemDTO itemDto = new SimpleItemDTO();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");

        // When
        SimpleItemDTO savedItem = itemService.saveItem(itemDto);

        // Then
        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isNotNull();
        assertThat(savedItem.getName()).isEqualTo("Test Item");
        assertThat(savedItem.getDescription()).isEqualTo("Test Description");
    }

    @Test
    @DisplayName("Debe crear un nuevo item dentro de un contenedor")
    void shouldCreateNewItemInContainer() {
        // Given
        SimpleItemDTO itemDto = new SimpleItemDTO();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setContainerId(testContainer.getId());

        // When
        SimpleItemDTO savedItem = itemService.saveItem(itemDto);

        // Then
        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isNotNull();
        assertThat(savedItem.getContainerId()).isEqualTo(testContainer.getId());
    }

    @Test
    @DisplayName("Debe obtener item por ID")
    void shouldGetItemById() {
        // Given
        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setTenant(testTenant);
        item = itemRepository.save(item);

        // When
        FullItemDTO foundItem = itemService.getItemById(testUser.getUsername(), item.getId());

        // Then
        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getId()).isEqualTo(item.getId());
        assertThat(foundItem.getName()).isEqualTo("Test Item");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando item no existe")
    void shouldThrowExceptionWhenItemNotFound() {
        // When & Then
        assertThatThrownBy(() -> itemService.getItemById(testUser.getUsername(), 999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Item no encontrado");
    }

    @Test
    @DisplayName("Debe buscar items por nombre")
    void shouldSearchItemsByName() {
        // Given
        Item item1 = new Item();
        item1.setName("Laptop HP");
        item1.setDescription("Test Description");
        item1.setTenant(testTenant);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Laptop Dell");
        item2.setDescription("Test Description");
        item2.setTenant(testTenant);
        itemRepository.save(item2);

        // When
        List<SimpleItemDTO> results = itemService.filterItems("Laptop");

        // Then
        assertThat(results).isNotEmpty();
        assertThat(results).hasSizeGreaterThanOrEqualTo(2);
        assertThat(results).extracting("name")
                .contains("Laptop HP", "Laptop Dell");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando no se encuentran items en búsqueda")
    void shouldThrowExceptionWhenNoItemsFoundInSearch() {
        // When & Then
        assertThatThrownBy(() -> itemService.filterItems("NoExiste"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("No se encontraron items");
    }

    @Test
    @DisplayName("Debe actualizar un item")
    void shouldUpdateItem() {
        // Given
        Item item = new Item();
        item.setName("Original Name");
        item.setDescription("Original Description");
        item.setTenant(testTenant);
        item = itemRepository.save(item);

        SimpleItemDTO updateData = new SimpleItemDTO();
        updateData.setName("Updated Name");
        updateData.setDescription("Updated Description");

        // When
        Item updatedItem = itemService.updateItem(item.getId(), updateData);

        // Then
        assertThat(updatedItem).isNotNull();
        assertThat(updatedItem.getName()).isEqualTo("Updated Name");
        assertThat(updatedItem.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    @DisplayName("Debe mover item a otro contenedor al actualizar")
    void shouldMoveItemToAnotherContainerOnUpdate() {
        // Given
        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setContainer(testContainer);
        item.setTenant(testTenant);
        item = itemRepository.save(item);

        Container newContainer = new Container();
        newContainer.setName("New Container");
        newContainer.setDescription("New Description");
        newContainer.setSector(testSector);
        newContainer.setTenant(testTenant);
        newContainer = containerRepository.save(newContainer);

        SimpleItemDTO updateData = new SimpleItemDTO();
        updateData.setContainerId(newContainer.getId());

        // When
        Item updatedItem = itemService.updateItem(item.getId(), updateData);

        // Then
        assertThat(updatedItem).isNotNull();
        assertThat(updatedItem.getContainer().getId()).isEqualTo(newContainer.getId());
    }

    @Test
    @DisplayName("Debe eliminar un item")
    void shouldDeleteItem() {
        // Given
        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setTenant(testTenant);
        item = itemRepository.save(item);
        Long itemId = item.getId();

        // When
        itemService.deleteItem(itemId);

        // Then
        assertThat(itemRepository.findById(itemId)).isEmpty();
    }

    @Test
    @DisplayName("Debe marcar item como en uso por un usuario")
    void shouldMarkItemAsInUse() {
        // Given
        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setTagId("TAG123");
        item.setTenant(testTenant);
        item = itemRepository.save(item);

        // When
        SimpleItemDTO result = itemService.markInUse("TAG123", testUser.getUsername());

        // Then
        assertThat(result).isNotNull();
        
        Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();
        assertThat(updatedItem.getInUseBy()).isNotNull();
        assertThat(updatedItem.getInUseBy().getId()).isEqualTo(testUser.getId());
    }

    @Test
    @DisplayName("Debe obtener items por ID de contenedor")
    void shouldGetItemsByContainerId() {
        // Given
        Item item1 = new Item();
        item1.setName("Item 1");
        item1.setDescription("Description 1");
        item1.setContainer(testContainer);
        item1.setTenant(testTenant);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Item 2");
        item2.setDescription("Description 2");
        item2.setContainer(testContainer);
        item2.setTenant(testTenant);
        itemRepository.save(item2);

        // When
        List<SimpleItemDTO> items = itemService.getItemsByContainerId(testContainer.getId());

        // Then
        assertThat(items).isNotEmpty();
        assertThat(items).hasSize(2);
    }

    @Test
    @DisplayName("Debe asignar tag RFID a item")
    void shouldAssignRfidTagToItem() {
        // Given
        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setTenant(testTenant);
        item = itemRepository.save(item);

        // When
        SimpleItemDTO result = itemService.assignRfidTagToItem(item.getId(), "RFID123");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTagId()).isEqualTo("RFID123");
    }

    @Test
    @DisplayName("Debe reasignar tag RFID cuando ya está asignado a otro item")
    void shouldReassignRfidTagWhenAlreadyAssigned() {
        // Given
        Item item1 = new Item();
        item1.setName("Item 1");
        item1.setDescription("Description 1");
        item1.setTagId("RFID123");
        item1.setTenant(testTenant);
        item1 = itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Item 2");
        item2.setDescription("Description 2");
        item2.setTenant(testTenant);
        item2 = itemRepository.save(item2);

        // When
        itemService.assignRfidTagToItem(item2.getId(), "RFID123");

        // Then
        Item updatedItem1 = itemRepository.findById(item1.getId()).orElseThrow();
        Item updatedItem2 = itemRepository.findById(item2.getId()).orElseThrow();
        
        assertThat(updatedItem1.getTagId()).isNull();
        assertThat(updatedItem2.getTagId()).isEqualTo("RFID123");
    }

    @Test
    @DisplayName("Debe obtener item por tag ID")
    void shouldGetItemByTagId() {
        // Given
        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setTagId("TAG456");
        item.setTenant(testTenant);
        item = itemRepository.save(item);

        // When
        SimpleItemDTO result = itemService.getItemByTagId("TAG456");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTagId()).isEqualTo("TAG456");
        assertThat(result.getName()).isEqualTo("Test Item");
    }
}

