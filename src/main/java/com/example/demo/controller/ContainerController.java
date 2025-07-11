package com.example.demo.controller;

import com.example.demo.dto.FullContainerDTO;
import com.example.demo.dto.Message;
import com.example.demo.dto.SimpleContainerDTO;
import com.example.demo.dto.SimpleItemDTO;
import com.example.demo.mapper.ContainerMapper;
import com.example.demo.mapper.ItemMapper;
import com.example.demo.model.Container;
import com.example.demo.model.Item;
import com.example.demo.service.ContainerService;
import com.example.demo.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContainerController {

    private ContainerService containerService;
    private ContainerMapper containerMapper;
    private ItemService itemService;

    public ContainerController(ContainerService containerService, ContainerMapper containerMapper, ItemService itemService){
        this.containerMapper = containerMapper;
        this.containerService = containerService;
        this.itemService = itemService;
    }
    @GetMapping("/containers")
    ResponseEntity<List<SimpleContainerDTO>> getAllContainers(){
        List<Container> results = containerService.getAllContainers();
        List<SimpleContainerDTO> resultData = containerMapper.toSimpleDTOList(results);
        return ResponseEntity.status(HttpStatus.OK).body(resultData);
    }
    @GetMapping("/containers/{containerId}")
    public ResponseEntity<FullContainerDTO> getContainerById(@PathVariable long containerId){
        Container container = containerService.getContainerById(containerId);
        FullContainerDTO containerData = containerMapper.toFullContainerDTO(container);
        return ResponseEntity.status(HttpStatus.OK).body(containerData);
    }
    @GetMapping("/containers/search")
    public ResponseEntity<List<SimpleContainerDTO>> getContainersByQuery(
            @RequestParam(required = false) String name
    ){
        List<Container> results = containerService.getContainersByName(name);
        List<SimpleContainerDTO> resultData = containerMapper.toSimpleDTOList(results);
        return ResponseEntity.ok(resultData);

    }
    @GetMapping("/containers/{containerId}/items")
    public ResponseEntity<List<SimpleItemDTO>> getItemFromContainerId(@PathVariable long containerId){
        List<SimpleItemDTO> itemList = itemService.getItemsByContainerId(containerId);
        return ResponseEntity.status(HttpStatus.OK).body(itemList);
    }
    @PostMapping("/containers")
    public ResponseEntity<SimpleContainerDTO> createContainer(@RequestBody SimpleContainerDTO containerData){
        Container newContainer = containerService.save(containerMapper.toContainerEntity(containerData));
        SimpleContainerDTO newContainerData = containerMapper.toSimpleDTO(newContainer);
        return ResponseEntity.ok(newContainerData);
    }
    @PostMapping("/containers/{containerId}/items")
    public ResponseEntity<SimpleItemDTO> createItemInContainer(@RequestBody SimpleItemDTO itemData,
                                                              @PathVariable long containerId){
        SimpleItemDTO savedItemData = itemService.saveItemInContainer(itemData,containerId);
        return ResponseEntity.ok(savedItemData);
    }

        @PatchMapping("/containers/{containerId}")
    public ResponseEntity<SimpleContainerDTO> updateContainer(@RequestBody SimpleContainerDTO changedData,
                                                              @PathVariable long containerId){
        Container updatedContainer = containerService.updateContainer(containerId,changedData);
        SimpleContainerDTO updatedContainerData = containerMapper.toSimpleDTO(updatedContainer);
        return ResponseEntity.ok(updatedContainerData);
    }
    @DeleteMapping("/containers/{containerId}")
    public ResponseEntity<Message> deleteContainer(@PathVariable long containerId){
        containerService.deleteById(containerId);
        return ResponseEntity.ok(new Message("Container Eliminado"));
    }
}
