package com.example.demo.controller;

import com.example.demo.dto.Message;
import com.example.demo.dto.SectorDTO;
import com.example.demo.service.SectorService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SectorController {

    private final SectorService sectorService;

    public SectorController(SectorService sectorService) {
        this.sectorService = sectorService;
    }

    @GetMapping("/sectors")
    public ResponseEntity<List<SectorDTO>> getAllSectors(){
        List<SectorDTO> resultList = sectorService.getAllSectors();
        return ResponseEntity.ok(resultList);
    }

    @GetMapping("/sectors/{sectorId}")
    public ResponseEntity<SectorDTO> getSectorById(@PathVariable long sectorId){
        SectorDTO sectorData = sectorService.getSectorById(sectorId);
        return ResponseEntity.ok(sectorData);
    }

    @PostMapping("/sectors")
    public ResponseEntity<SectorDTO> createSector(@RequestBody SectorDTO sectorData){
        SectorDTO newSector = sectorService.createSector(sectorData);
        return ResponseEntity.ok(newSector);
    }

    @DeleteMapping("/sectors/{sectorId}")
    public ResponseEntity<Message> deleteSector(@PathVariable long sectorId){
        sectorService.deleteSector(sectorId);
        return ResponseEntity.ok(new Message("Sector eliminado"));
    }

    // GET ALL CONTAINERS FROM A SECTOR
    // POST A CONTAINER DIRECTLY IN A SECTOR



}
