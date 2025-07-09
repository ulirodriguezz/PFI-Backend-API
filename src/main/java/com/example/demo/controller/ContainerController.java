package com.example.demo.controller;

import com.example.demo.dto.SimpleContainerDTO;
import com.example.demo.mapper.ContainerMapper;
import com.example.demo.model.Container;
import com.example.demo.service.ContainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContainerController {

    private ContainerService containerService;
    private ContainerMapper containerMapper;

    public ContainerController(ContainerService containerService, ContainerMapper containerMapper){
        this.containerMapper = containerMapper;
        this.containerService = containerService;
    }
    @GetMapping("/containers")
    ResponseEntity<List<SimpleContainerDTO>> getAllContainers(){
        List<Container> results = containerService.getAllContainers();
        List<SimpleContainerDTO> resultData = containerMapper.toSimpleDTOList(results);
        return ResponseEntity.status(HttpStatus.OK).body(resultData);
    }
}
