package com.example.demo.controller;

import com.example.demo.dto.Message;
import com.example.demo.dto.SimpleItemDTO;
import com.example.demo.mapper.ItemMapper;
import com.example.demo.model.Item;
import com.example.demo.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;
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
    @GetMapping("/items/{itemId}")
    public ResponseEntity<SimpleItemDTO> getItemById(@PathVariable long itemId){
        Item storedItem = itemService.getItemById(itemId);
        SimpleItemDTO itemData = itemMapper.toSimpleItemDTO(storedItem);
        return ResponseEntity.status(HttpStatus.OK).body(itemData);
    }
    @GetMapping("items/search")
    public ResponseEntity<List<SimpleItemDTO>> filterItems(
            @RequestParam(required = false,defaultValue = "") String name,
            @RequestParam(required = false,defaultValue = "") String description
    ){
        List<Item> matchingItems = itemService.filterItems(name,description);
        List<SimpleItemDTO> resultData = itemMapper.toSimpleItemDtoList(matchingItems);
        return ResponseEntity.status(HttpStatus.OK).body(resultData);
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<SimpleItemDTO> updateItem(@PathVariable long ItemId, @RequestBody SimpleItemDTO itemData){
        Item updatedItem = itemService.updateItem(ItemId,itemData);
        SimpleItemDTO updatedItemData = itemMapper.toSimpleItemDTO(updatedItem);
        return ResponseEntity.status(HttpStatus.OK).body(updatedItemData);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Message> deleteItem(@PathVariable long itemId){
        itemService.deleteItem(itemId);
        return ResponseEntity.status(HttpStatus.OK).body(new Message("Item eliminado"));
    }
}
