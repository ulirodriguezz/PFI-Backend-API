package com.example.demo.service;

import com.example.demo.dto.SimpleContainerDTO;
import com.example.demo.mapper.ContainerMapper;
import com.example.demo.model.Container;
import com.example.demo.repository.ContainerRepository;
import com.example.demo.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContainerService {
    private ContainerRepository containerRepository;
    private ContainerMapper containerMapper;

    private ItemRepository itemRepository;

    public ContainerService(ContainerRepository containerRepository, ContainerMapper containerMapper, ItemRepository itemRepository){
        this.containerRepository = containerRepository;
        this.containerMapper = containerMapper;
        this.itemRepository = itemRepository;
    }
    public List<Container> getAllContainers(){
        List<Container> results = containerRepository.findAll();
        if(results.isEmpty())
            throw new EntityNotFoundException("No hay contenedores");
        return results;
    }
    public Container getContainerById(long id){
        Container storedContainer = containerRepository.getContainerById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el contenedor"));
        return storedContainer;
    }
    public List<Container> getContainersByName(String name){
        List<Container> mathingResults = containerRepository.getAllByNameContainingIgnoreCase(name);
        if(mathingResults.isEmpty())
            throw new EntityNotFoundException("No se encontraron contenedores");
        return mathingResults;
    }
    @Transactional
    public Container save (Container newContainer){
        return containerRepository.save(newContainer);
    }
    @Transactional
    public Container updateContainer(long id, SimpleContainerDTO changedData){
        Container oldContainer = containerRepository.getContainerById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el contenedor"));
        containerMapper.mergeChanges(oldContainer,changedData);
        return containerRepository.save(oldContainer);
    }
    @Transactional
    public void deleteById(long id){
        itemRepository.clearContainerReferenceFromItems(id);
        containerRepository.deleteById(id);
    }
}
