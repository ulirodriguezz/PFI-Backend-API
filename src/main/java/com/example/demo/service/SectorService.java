package com.example.demo.service;

import com.example.demo.dto.FullContainerDTO;
import com.example.demo.dto.FullSectorDTO;
import com.example.demo.dto.SimpleSectorDTO;
import com.example.demo.helpers.TenantContext;
import com.example.demo.mapper.ContainerMapper;
import com.example.demo.mapper.SectorMapper;
import com.example.demo.model.Sector;
import com.example.demo.model.Tenant;
import com.example.demo.repository.ContainerRepository;
import com.example.demo.repository.SectorRepository;
import com.example.demo.repository.TenantRepository;
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
    private final TenantRepository tenantRepository;

    public SectorService(SectorRepository sectorRepository, SectorMapper sectorMapper, ContainerRepository containerRepository, ContainerMapper containerMapper, TenantRepository tenantRepository) {
        this.sectorRepository = sectorRepository;
        this.sectorMapper = sectorMapper;
        this.containerRepository = containerRepository;
        this.containerMapper = containerMapper;
        this.tenantRepository = tenantRepository;
    }

    public List<SimpleSectorDTO> getAllSectors() {
        //TenantRepository getTenantById()
        List<Sector> sectorsList = sectorRepository.findSectorsByTenantId(1);
        return sectorMapper.toSimpleSectorDtoList(sectorsList);
    }

    public List<SimpleSectorDTO> getAllSectorsByQuery(String query) {
        if(TenantContext.getTenantId() != 1)
            return this.findByQueryAndTenant(query);
        List<Sector> results = sectorRepository.findAllByNameContainsOrDescriptionContaining(query, query);
        if (results.isEmpty())
            throw new EntityNotFoundException("No se encontraron sectores para la busqueda: " + query);
        return sectorMapper.toSimpleSectorDtoList(results);
    }

    private List<SimpleSectorDTO> findByQueryAndTenant(String query) {
        Tenant userTenant = tenantRepository.findById(TenantContext.getTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Tenant No Encontrado"));

        List<Sector> results = sectorRepository.findAllByQueryAndTenant(query, userTenant);
        if (results.isEmpty())
            throw new EntityNotFoundException("No se encontraron sectores para la busqueda: " + query);
        return sectorMapper.toSimpleSectorDtoList(results);
    }

    @Transactional
    public FullSectorDTO getSectorById(long sectorId) {
        Sector sector = sectorRepository.getSectorById(sectorId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el sector"));
        FullSectorDTO dto = sectorMapper.toFullSectorDTO(sector);
        dto.setContainers(containerMapper.toContainerPreviewList(containerRepository.getAllBySector(sector)));
        return dto;
    }

    @Transactional
    public SimpleSectorDTO createSector(SimpleSectorDTO sectorData) {
        Tenant tenant = tenantRepository.findById(TenantContext.getTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Tenant No Encontrado"));

        Sector newSector = sectorMapper.toSectorEntity(sectorData);
        newSector.setTenant(tenant);

        return sectorMapper.toSimpleSectorDTO(sectorRepository.save(newSector));
    }

    @Transactional
    public void deleteSector(long sectorId) {
        containerRepository.clearSectorReferenceFromContainer(sectorId);
        sectorRepository.deleteById(sectorId);
    }


    public SimpleSectorDTO patchSector(long sectorId, SimpleSectorDTO updatedSectorData) {
        Sector sectorInDB = sectorRepository.getSectorById(sectorId)
                .orElseThrow(() -> new EntityNotFoundException("No se ecnontro el Sector"));
        sectorMapper.mergeChanges(sectorInDB, updatedSectorData);

        Sector updatedSector = sectorRepository.save(sectorInDB);
        return sectorMapper.toSimpleSectorDTO(updatedSector);

    }
}
