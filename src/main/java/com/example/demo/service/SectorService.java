package com.example.demo.service;

import com.example.demo.dto.SectorDTO;
import com.example.demo.mapper.SectorMapper;
import com.example.demo.model.Sector;
import com.example.demo.repository.ContainerRepository;
import com.example.demo.repository.SectorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorService {
    private SectorRepository sectorRepository;
    private ContainerRepository containerRepository;
    private SectorMapper sectorMapper;

    public SectorService(SectorRepository sectorRepository, ContainerRepository containerRepository, SectorMapper sectorMapper) {
        this.sectorRepository = sectorRepository;
        this.containerRepository = containerRepository;
        this.sectorMapper = sectorMapper;
    }

    public List<SectorDTO> getAllSectors(){
        List<Sector> sectorsList = sectorRepository.findAll();
        return sectorMapper.toSectorDtoList(sectorsList);
    }
    public SectorDTO getSectorById(long sectorId){
        Sector sector = sectorRepository.getSectorById(sectorId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el sector"));
        return sectorMapper.toSectorDTO(sector);
    }
    @Transactional
    public SectorDTO createSector(SectorDTO sectorData){
        Sector newSector = sectorMapper.toSectorEntity(sectorData);
        Sector storedSector = sectorRepository.save(newSector);
        return sectorMapper.toSectorDTO(storedSector);
    }
    @Transactional
    public void deleteSector(long sectorId){
        containerRepository.clearSectorReferenceFromContainer(sectorId);
        sectorRepository.deleteById(sectorId);
    }


}
