package com.example.demo.controller;

import com.example.demo.dto.Message;
import com.example.demo.dto.MovementAppPostDTO;
import com.example.demo.dto.MovementGetDTO;
import com.example.demo.dto.MovementPostDTO;
import com.example.demo.service.ItemService;
import com.example.demo.service.MovementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovementController {

    private final MovementService movementService;

    public MovementController(MovementService movementService, ItemService itemService){
        this.movementService = movementService;
    }

    @GetMapping("/movements/items/{itemId}")
    public ResponseEntity<List<MovementGetDTO>> getAllMovementsFromItem(@PathVariable long itemId){
        List<MovementGetDTO> results = movementService.getAllMovementsByItemId(itemId);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/movements")
    public ResponseEntity<MovementPostDTO> registerMovement(@RequestBody MovementPostDTO movementData){
        MovementPostDTO registeredMovement = movementService.registerNewMovement(movementData);
        return ResponseEntity.ok(registeredMovement);
    }
    @PostMapping("/movements/app")
    public ResponseEntity<MovementAppPostDTO> registerMovementFromApp(@RequestBody MovementAppPostDTO movementData){
        MovementAppPostDTO registeredMovement = movementService.registerNewMovement(movementData);
        return ResponseEntity.ok(registeredMovement);
    }

    @DeleteMapping("/movements/{movementId}")
    public ResponseEntity<Message> deleteMovementByID(@PathVariable long movementId){
        movementService.deleteMovementById(movementId);
        return ResponseEntity.ok(new Message("Movimiento eliminado del historial"));
    }

}
