package com.example.demo.mapper;

import com.example.demo.dto.MovementDTO;
import com.example.demo.model.Container;
import com.example.demo.model.Item;
import com.example.demo.model.Movement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovementMapper {
    public Movement toEntity(MovementDTO dto, Container container, Item item){
        Movement movement = new Movement();
        movement.setItem(item);
        movement.setDestinationContainer(container);
        return movement;
    }

    public MovementDTO toDTO (Movement movement){
        MovementDTO dto = new MovementDTO();
        dto.setId(movement.getId());
        dto.setContainerReaderId(movement.getDestinationContainer().getReaderId());
        dto.setTagId(movement.getItem().getTagId());
        dto.setTimestamp(movement.getTimestamp());
        return dto;
    }
    public List<MovementDTO> toMovementDtoList (List<Movement> movements){
        List<MovementDTO> dtos = new ArrayList<>();
        for(Movement m : movements){
            dtos.add(this.toDTO(m));
        }
        return dtos;
    }
}
