package com.example.demo.controller;

import com.example.demo.dto.Message;
import com.example.demo.dto.MovementDTO;
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

    @GetMapping("/items/{itemId}/movements")
    public ResponseEntity<List<MovementDTO>> getAllMovementsFromItem(@PathVariable long itemId){
        List<MovementDTO> results = movementService.getAllMovementsByItemId(itemId);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/items/movements")
    public ResponseEntity<MovementDTO> registerMovement(@RequestBody MovementDTO movementData){
        MovementDTO registeredMovement = movementService.registerNewMovement(movementData);
        return ResponseEntity.ok(registeredMovement);
    }

    @DeleteMapping("/items/movements/{movementId}")
    public ResponseEntity<Message> deleteMovementByID(@PathVariable long movementId){
        movementService.deleteMovementById(movementId);
        return ResponseEntity.ok(new Message("Movimiento eliminado del historial"));
    }

}
