package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.mapper.ContainerMapper;
import com.example.demo.model.Container;
import com.example.demo.service.ContainerService;
import com.example.demo.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ContainerController {

    private final ContainerService containerService;
    private final ContainerMapper containerMapper;
    private final ItemService itemService;

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
        FullContainerDTO containerData = containerService.getContainerById(containerId);
        return ResponseEntity.status(HttpStatus.OK).body(containerData);
    }
    @GetMapping("/containers/search")
    public ResponseEntity<List<SimpleContainerDTO>> getContainersByQuery(
            @RequestParam(required = false, defaultValue = "") String query
    ){
        List<SimpleContainerDTO> resultData = containerService.getFilteredContainers(query);
        return ResponseEntity.ok(resultData);
    }

    @GetMapping("/containers/{containerId}/items")
    public ResponseEntity<List<SimpleItemDTO>> getItemFromContainerId(@PathVariable long containerId){
        List<SimpleItemDTO> itemList = itemService.getItemsByContainerId(containerId);
        return ResponseEntity.status(HttpStatus.OK).body(itemList);
    }
    @PostMapping("/containers")
    public ResponseEntity<SimpleContainerDTO> createContainer(@RequestBody SimpleContainerDTO containerData){
        SimpleContainerDTO result;
        if(containerData.getSectorId() != null)
        {
           result = containerService.saveContainerInSector(containerData.getSectorId(),containerData);
        }else {
            result = containerService.save(containerData);
        }
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/containers/{containerId}")
    public ResponseEntity<SimpleContainerDTO> updateContainer(@RequestBody SimpleContainerDTO changedData,
                                                              @PathVariable long containerId){
        SimpleContainerDTO updatedContainer = containerService.updateContainer(containerId,changedData);
        return ResponseEntity.ok(updatedContainer);
    }
    @DeleteMapping("/containers/{containerId}")
    public ResponseEntity<Message> deleteContainer(@PathVariable long containerId){
        containerService.deleteById(containerId);
        return ResponseEntity.ok(new Message("Container Eliminado"));
    }

//    @PostMapping("/containers/{containerId}/images")
//    public ResponseEntity<ImageDTO> uploadImageToItem(@RequestParam MultipartFile imageFile, @PathVariable long containerId)
//    {
//        ImageDTO createdImage = containerService.addImageToContainer(containerId,imageFile);
//        return ResponseEntity.ok(createdImage);
//
//    }
//    @GetMapping("/containers/{containerId}/images")
//    public ResponseEntity<List<ImageDTO>> getAllImagesFromItem(@PathVariable long containerId)
//    {
//        List<ImageDTO> results = containerService.getAllImagesFromContainer(containerId);
//        return ResponseEntity.ok(results);
//    }
//
//    @DeleteMapping("/containers/{containerId}/images/{imageId}")
//    public ResponseEntity<Message> deleteImageFromItem(@PathVariable long containerId, @PathVariable long imageId){
//        containerService.deleteImageFromContainer(imageId,containerId);
//        return ResponseEntity.ok(new Message("Imagen Eliminada Con Exito"));
//    }

}
