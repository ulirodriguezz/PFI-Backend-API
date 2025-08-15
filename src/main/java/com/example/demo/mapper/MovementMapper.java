package com.example.demo.mapper;

import com.example.demo.dto.MovementGetDTO;
import com.example.demo.dto.MovementPostDTO;
import com.example.demo.model.Container;
import com.example.demo.model.Item;
import com.example.demo.model.Movement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovementMapper {
    public Movement toEntity(MovementPostDTO dto, Container container, Item item){
        Movement movement = new Movement();
        movement.setItem(item);
        movement.setDestinationContainer(container);
        return movement;
    }

    public MovementPostDTO toMovementPostDTO(Movement movement){
        MovementPostDTO dto = new MovementPostDTO();
        dto.setId(movement.getId());
        dto.setContainerReaderId(movement.getDestinationContainer().getReaderId());
        dto.setTagId(movement.getItem().getTagId());
        dto.setTimestamp(movement.getTimestamp());
        return dto;
    }
    public MovementGetDTO toMovementGetDTO(Movement movement){
        MovementGetDTO dto = new MovementGetDTO();
        dto.setId(movement.getId());
        dto.setContainerId(movement.getDestinationContainer().getId());
        dto.setItemId(movement.getItem().getId());
        dto.setTimestamp(movement.getTimestamp());
        return dto;
    }
    public List<MovementGetDTO> toMovementGetDTOList(List<Movement> movements){
        List<MovementGetDTO> dtos = new ArrayList<>();
        for(Movement m : movements){
            dtos.add(this.toMovementGetDTO(m));
        }
        return dtos;
    }
}
