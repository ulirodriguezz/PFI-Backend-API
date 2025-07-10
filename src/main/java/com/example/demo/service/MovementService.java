package com.example.demo.service;

import com.example.demo.dto.MovementDTO;
import com.example.demo.mapper.MovementMapper;
import com.example.demo.model.Container;
import com.example.demo.model.Item;
import com.example.demo.model.Movement;
import com.example.demo.repository.ContainerRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.MovementRepository;
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
    public MovementDTO addMovement (long itemId, MovementDTO movementData){
        Container destinationContainerRef = containerRepository.getReferenceById(movementData.getContainerId());
        Item itemRef = itemRepository.getReferenceById(itemId);
        Movement newMovement = movementMapper.toEntity(movementData,destinationContainerRef,itemRef);
        Movement storedMovement = movementRepository.save(newMovement);
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
