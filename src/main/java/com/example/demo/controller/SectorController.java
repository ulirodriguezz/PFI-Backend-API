package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.ContainerService;
import com.example.demo.service.SectorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SectorController {

    private final SectorService sectorService;

    private final ContainerService containerService;

    public SectorController(SectorService sectorService,ContainerService containerService) {
        this.sectorService = sectorService;
        this.containerService = containerService;
    }

    @GetMapping("/sectors")
    public ResponseEntity<List<SimpleSectorDTO>> getAllSectors(){
        List<SimpleSectorDTO> resultList = sectorService.getAllSectors();
        return ResponseEntity.ok(resultList);
    }
    @PostMapping("/sectors")
    public ResponseEntity<SimpleSectorDTO> createSector(@RequestBody SimpleSectorDTO sectorData){
        SimpleSectorDTO newSector = sectorService.createSector(sectorData);
        return ResponseEntity.ok(newSector);
    }

    @GetMapping("/sectors/{sectorId}")
    public ResponseEntity<FullSectorDTO> getSectorById(@PathVariable long sectorId){
        FullSectorDTO sectorData = sectorService.getSectorById(sectorId);
        return ResponseEntity.ok(sectorData);
    }

    @DeleteMapping("/sectors/{sectorId}")
    public ResponseEntity<Message> deleteSector(@PathVariable long sectorId){
        sectorService.deleteSector(sectorId);
        return ResponseEntity.ok(new Message("Sector eliminado"));
    }




}
