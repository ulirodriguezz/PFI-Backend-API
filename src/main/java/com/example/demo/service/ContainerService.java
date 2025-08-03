package com.example.demo.service;

import com.example.demo.dto.FullContainerDTO;
import com.example.demo.dto.SimpleContainerDTO;
import com.example.demo.dto.SimpleSectorDTO;
import com.example.demo.mapper.ContainerMapper;
import com.example.demo.mapper.ItemMapper;
import com.example.demo.model.Container;
import com.example.demo.model.Item;
import com.example.demo.model.Sector;
import com.example.demo.repository.ContainerRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.SectorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContainerService {
    private ContainerRepository containerRepository;
    private ContainerMapper containerMapper;

    private ItemMapper itemMapper;
    private ItemRepository itemRepository;

    private SectorRepository sectorRepository;

    public ContainerService(ContainerRepository containerRepository, ContainerMapper containerMapper, ItemRepository itemRepository, ItemMapper itemMapper, SectorRepository sectorRepository){
        this.containerRepository = containerRepository;
        this.containerMapper = containerMapper;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.sectorRepository = sectorRepository;
    }
    public List<Container> getAllContainers(){
        List<Container> results = containerRepository.findAll();
        if(results.isEmpty())
            throw new EntityNotFoundException("No hay contenedores");
        return results;
    }

    @Transactional
    public FullContainerDTO getContainerById(long id){
        Container storedContainer = containerRepository.getContainerById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el contenedor"));
        FullContainerDTO dto = containerMapper.toFullContainerDTO(storedContainer);

        List<Item> containerItems = itemRepository.getItemByContainerId(storedContainer.getId());

        dto.setItems(itemMapper.toItemPreviewList(containerItems));
        return dto;
    }
    public List<Container> getContainersByName(String name){
        List<Container> mathingResults = containerRepository.getAllByNameContainingIgnoreCase(name);
        if(mathingResults.isEmpty())
            throw new EntityNotFoundException("No se encontraron contenedores");
        return mathingResults;
    }
    @Transactional
    public SimpleContainerDTO save (SimpleContainerDTO newContainerData){
        Container newContainer = containerMapper.toContainerEntity(newContainerData);
        return containerMapper.toSimpleDTO(containerRepository.save(newContainer));
    }
    @Transactional
    public Container updateContainer(long id, SimpleContainerDTO changedData){
        Container oldContainer = containerRepository.getContainerById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el contenedor"));
        containerMapper.mergeChanges(oldContainer,changedData);
        if(changedData.getSectorId() != null)
            this.changeContainerSector(oldContainer,changedData.getSectorId());

        return containerRepository.save(oldContainer);
    }
    @Transactional
    public void deleteById(long id){
        itemRepository.clearContainerReferenceFromItems(id);
        containerRepository.deleteById(id);
    }

    @Transactional
    public SimpleContainerDTO saveContainerInSector(long sectorId, SimpleContainerDTO containerData) {
        Sector sector = sectorRepository.getSectorById(sectorId)
                .orElseThrow(() -> new EntityNotFoundException("Sector Not Found"));
        Container newContainer = containerMapper.toContainerEntity(containerData);
        newContainer.setSector(sector);
        Container storedContainer = containerRepository.save(newContainer);

        return containerMapper.toSimpleDTO(storedContainer);

    }

    private void changeContainerSector(Container container, Long sectorId){
        Sector destinationSector = sectorRepository.getSectorById(sectorId)
                .orElseThrow(() -> new EntityNotFoundException("Sector Not Found"));
        container.setSector(destinationSector);
    }

}
