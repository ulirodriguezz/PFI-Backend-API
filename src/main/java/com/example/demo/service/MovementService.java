package com.example.demo.service;

import com.example.demo.dto.MovementDTO;
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

    public List<MovementDTO> getAllMovementsByItemId(long itemId){
        List<Movement> movements = movementRepository.getAllByItemIdOrderByTimestampDesc(itemId);
        return movementMapper.toMovementDtoList(movements);
    }
    @Transactional
    public MovementDTO registerNewMovement(MovementDTO movementData) {
        Item item = itemRepository.getByTagId(movementData.getTagId())
                .orElseThrow(() -> new EntityNotFoundException("Item Not Found"));
        Container destinationContainer = containerRepository.getContainerByReaderId(movementData.getContainerReaderId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el container"));
        item.setContainer(destinationContainer);
        Movement newMovement = movementMapper.toEntity(movementData, destinationContainer, item);
        Movement storedMovement = movementRepository.save(newMovement);
        itemRepository.save(item);
        return movementMapper.toDTO(storedMovement);
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
