package com.example.demo.service;

import com.example.demo.dto.MovementAppPostDTO;
import com.example.demo.dto.MovementGetDTO;
import com.example.demo.dto.MovementPostDTO;
import com.example.demo.mapper.MovementMapper;
import com.example.demo.model.Container;
import com.example.demo.model.Item;
import com.example.demo.model.Movement;
import com.example.demo.repository.ContainerRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.MovementRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class MovementService {
    private MovementRepository movementRepository;

    private ContainerRepository containerRepository;
    private ItemRepository itemRepository;
    private MovementMapper movementMapper;

    public MovementService(MovementMapper movementMapper,
                           MovementRepository movementRepository,
                           ContainerRepository containerRepository,
                           ItemRepository itemRepository){
        this.movementMapper = movementMapper;
        this.movementRepository = movementRepository;
        this.itemRepository = itemRepository;
        this.containerRepository = containerRepository;
    }

    public List<MovementGetDTO> getAllMovementsByItemId(long itemId){
        List<Movement> movements = movementRepository.getAllByItemIdOrderByTimestampDesc(itemId);
        return movementMapper.toMovementGetDTOList(movements);
    }

    /**
     * Registers an item being stored in a destination container
     * @param movementData DTO containing the item's associated NFC tag id and the container's reader id
     * @return a MovementDTO with all the relevant information of the movement
     */
    @Transactional
    public MovementPostDTO registerNewMovement(MovementPostDTO movementData) {
        Item item = itemRepository.getByTagId(movementData.getTagId())
                .orElseThrow(() -> new EntityNotFoundException("Item Not Found"));
        Container destinationContainer = containerRepository.getContainerByReaderId(movementData.getContainerReaderId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el container"));

        item.setContainer(destinationContainer);
        item.setInUseBy(null);

        Movement newMovement = movementMapper.toEntity(movementData, destinationContainer, item);
        Movement storedMovement = movementRepository.save(newMovement);

        itemRepository.save(item);

        return movementMapper.toMovementPostDTO(storedMovement);
    }
    @Transactional
    public MovementAppPostDTO registerNewMovement(MovementAppPostDTO movementData) {
        Item item = itemRepository.getByTagId(movementData.getTagId())
                .orElseThrow(() -> new EntityNotFoundException("Item Not Found"));
        Container destinationContainer = containerRepository.getContainerById(movementData.getContainerId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el container"));

        item.setContainer(destinationContainer);
        item.setInUseBy(null);

        Movement newMovement = movementMapper.toEntity(movementData, destinationContainer, item);
        Movement storedMovement = movementRepository.save(newMovement);

        itemRepository.save(item);

        return movementMapper.toMovementAppPostDTO(storedMovement);
    }
    @Transactional
    public MovementPostDTO registerNewMovement(Item item, Container newLocation) {
        Movement movement = new Movement();
        movement.setItem(item);
        movement.setDestinationContainer(newLocation);
        movement.setTimestamp(Instant.now());
        return movementMapper.toMovementPostDTO(movementRepository.save(movement));
    }
    @Transactional
    public void deleteMovementById(long movementId){
        movementRepository.deleteById(movementId);
    }
    @Transactional
    public void deleteAllMovementsByItemId(long itemId){
        movementRepository.deleteAllByItemId(itemId);
    }


}
