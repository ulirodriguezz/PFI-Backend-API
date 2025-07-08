package com.example.demo.controller;

import com.example.demo.dto.SimpleItemDTO;
import com.example.demo.mapper.ItemMapper;
import com.example.demo.model.Item;
import com.example.demo.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

    ItemService itemService;
    ItemMapper itemMapper;
    public ItemController(ItemService itemService, ItemMapper itemMapper){
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }
    @PostMapping("/items")
    public ResponseEntity<SimpleItemDTO> saveItem(@RequestBody SimpleItemDTO itemData){
        Item newItem = itemMapper.toItemEntity(itemData);
        Item storedItem =itemService.saveItem(newItem);
        SimpleItemDTO storedItemData = itemMapper.toSimpleItemDTO(storedItem);
        return ResponseEntity.status(HttpStatus.OK).body(storedItemData);
    }
}
