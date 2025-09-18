package com.example.demo.controller;

import com.example.demo.config.JwtProvider;
import com.example.demo.dto.FullItemDTO;
import com.example.demo.dto.ImageDTO;
import com.example.demo.dto.Message;
import com.example.demo.dto.SimpleItemDTO;
import com.example.demo.mapper.ItemMapper;
import com.example.demo.model.Item;
import com.example.demo.service.ItemService;
import com.example.demo.service.MovementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private  final JwtProvider jwtProvider;

    public ItemController(ItemService itemService, ItemMapper itemMapper, JwtProvider jwtProvider) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/items")
    public ResponseEntity<SimpleItemDTO> saveItem(@RequestBody SimpleItemDTO itemData){
        SimpleItemDTO storedItem = itemService.saveItem(itemData);
        return ResponseEntity.status(HttpStatus.OK).body(storedItem);
    }
    @GetMapping("/items/{itemId}")
    public ResponseEntity<FullItemDTO> getItemById(@RequestHeader("Authorization") String authHeader,@PathVariable long itemId){
        String token = jwtProvider.getTokenFromHeader(authHeader);
        String loggedUsername = jwtProvider.getUsernameFromToken(token);
        FullItemDTO result = itemService.getItemById(loggedUsername,itemId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @GetMapping("items/search")
    public ResponseEntity<List<SimpleItemDTO>> filterItems(
            @RequestParam(required = false,defaultValue = "") String query
    ){
        List<Item> matchingItems = itemService.filterItems(query);
        List<SimpleItemDTO> resultData = itemMapper.toSimpleItemDtoList(matchingItems);
        return ResponseEntity.status(HttpStatus.OK).body(resultData);
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<SimpleItemDTO> updateItem(@PathVariable long itemId, @RequestBody SimpleItemDTO itemData){
        Item updatedItem = itemService.updateItem(itemId,itemData);
        SimpleItemDTO updatedItemData = itemMapper.toSimpleItemDTO(updatedItem);
        return ResponseEntity.status(HttpStatus.OK).body(updatedItemData);
    }
    @PatchMapping("/items/{itemId}/tag")
    public ResponseEntity<SimpleItemDTO> asingRfidTagToItem(@PathVariable long itemId, @RequestBody SimpleItemDTO tagData){
        SimpleItemDTO updatedItem = itemService.assignRfidTagToItem(itemId,tagData.getTagId());
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Message> deleteItem(@PathVariable long itemId){
        itemService.deleteItem(itemId);
        return ResponseEntity.status(HttpStatus.OK).body(new Message("Item eliminado"));
    }

    @PostMapping("/items/{itemId}/images")
    public ResponseEntity<ImageDTO> uploadImageToItem(@RequestParam MultipartFile imageFile, @PathVariable long itemId)
    {
        ImageDTO createdImage = itemService.addImageToItem(itemId,imageFile);
        return ResponseEntity.ok(createdImage);

    }
    @GetMapping("/items/{itemId}/images")
    public ResponseEntity<List<ImageDTO>> getAllImagesFromItem(@PathVariable long itemId)
    {
        List<ImageDTO> results = itemService.getAllImagesFromItem(itemId);
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/items/{itemId}/images/{imageId}")
    public ResponseEntity<Message> deleteImageFromItem(@PathVariable long itemId, @PathVariable long imageId){
        itemService.deleteImageFromItem(imageId,itemId);
        return ResponseEntity.ok(new Message("Imagen Eliminada Con Exito"));
    }


}
