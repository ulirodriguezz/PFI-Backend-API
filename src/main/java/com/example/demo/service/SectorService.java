package com.example.demo.service;

import com.example.demo.dto.FullContainerDTO;
import com.example.demo.dto.FullSectorDTO;
import com.example.demo.dto.SimpleSectorDTO;
import com.example.demo.mapper.ContainerMapper;
import com.example.demo.mapper.SectorMapper;
import com.example.demo.model.Sector;
import com.example.demo.model.Tenant;
import com.example.demo.repository.ContainerRepository;
import com.example.demo.repository.SectorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorService {
    private final SectorRepository sectorRepository;
    private final SectorMapper sectorMapper;
    private final ContainerRepository containerRepository;
    private final ContainerMapper containerMapper;

    public SectorService(SectorRepository sectorRepository, ContainerRepository containerRepository, SectorMapper sectorMapper, ContainerMapper containerMapper) {
        this.sectorRepository = sectorRepository;
        this.containerRepository = containerRepository;
        this.sectorMapper = sectorMapper;
        this.containerMapper = containerMapper;
    }

    public List<SimpleSectorDTO> getAllSectors(){
        //TenantRepository getTenantById()
        List<Sector> sectorsList = sectorRepository.findSectorsByTenantId(1);
        return sectorMapper.toSimpleSectorDtoList(sectorsList);
    }

    public List<SimpleSectorDTO> getAllSectorsByQuery(String query){
        List<Sector> results = sectorRepository.findAllByNameContainsOrDescriptionContaining(query,query);
        if(results.isEmpty())
            throw new EntityNotFoundException("No se encontraron sectores para la busqueda: "+query);
        return sectorMapper.toSimpleSectorDtoList(results);
    }
    @Transactional
    public FullSectorDTO getSectorById(long sectorId){
        Sector sector = sectorRepository.getSectorById(sectorId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el sector"));
        FullSectorDTO dto = sectorMapper.toFullSectorDTO(sector);
        dto.setContainers(containerMapper.toContainerPreviewList(containerRepository.getAllBySector(sector)));
        return dto;
    }
    @Transactional
    public SimpleSectorDTO createSector(SimpleSectorDTO sectorData){
        Tenant tenant = new Tenant();
        tenant.setId(1);
        tenant.setName("UADE");

        Sector newSector = sectorMapper.toSectorEntity(sectorData);
        newSector.setTenant(tenant);
        Sector storedSector = sectorRepository.save(newSector);
        return sectorMapper.toSimpleSectorDTO(storedSector);
    }
    @Transactional
    public void deleteSector(long sectorId){
        containerRepository.clearSectorReferenceFromContainer(sectorId);
        sectorRepository.deleteById(sectorId);
    }


    public SimpleSectorDTO patchSector(long sectorId,SimpleSectorDTO updatedSectorData) {
        Sector sectorInDB = sectorRepository.getSectorById(sectorId)
                .orElseThrow(() -> new EntityNotFoundException("No se ecnontro el Sector"));
        sectorMapper.mergeChanges(sectorInDB,updatedSectorData);

        Sector updatedSector = sectorRepository.save(sectorInDB);
        return sectorMapper.toSimpleSectorDTO(updatedSector);

    }
}
