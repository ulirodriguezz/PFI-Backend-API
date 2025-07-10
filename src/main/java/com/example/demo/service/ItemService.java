package com.example.demo.service;

import com.example.demo.dto.MovementDTO;
import com.example.demo.dto.SimpleItemDTO;
import com.example.demo.mapper.ItemMapper;
import com.example.demo.model.Container;
import com.example.demo.model.Item;
import com.example.demo.repository.ContainerRepository;
import com.example.demo.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ItemService {
    private ItemRepository itemRepository;
    private ContainerRepository containerRepository;
    private ItemMapper itemMapper;

    private MovementService movementService;
    public ItemService(ItemRepository itemRepository,
                       ItemMapper itemMapper,
                       ContainerRepository containerRepository,
                       MovementService movementService){
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.containerRepository = containerRepository;
        this.movementService = movementService;
    }
    @Transactional
    public Item saveItem(Item item){
        Item savedItem = this.itemRepository.save(item);
        return savedItem;
    }
    @Transactional
    public SimpleItemDTO saveItemInContainer(SimpleItemDTO itemData,long containerId){
        Item newItem = itemMapper.toItemEntity(itemData);
        Container container = containerRepository.getContainerById(containerId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el contenedor"));
        newItem.setContainer(container);
        Item savedItem = itemRepository.save(newItem);
        return itemMapper.toSimpleItemDTO(savedItem);
    }
    @Transactional
    public void changeItemLocation(long itemId, MovementDTO movementData){
        Item item = itemRepository.getItemById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el item"));
        Container destinationContainer = containerRepository.getContainerById(movementData.getContainerId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el container"));
        item.setContainer(destinationContainer);
        movementService.addMovement(itemId,movementData);
    }

    public Item getItemById(long id){
        Item item = itemRepository.getItemById(id)
                .orElseThrow(()-> new EntityNotFoundException("Item no encontrado"));
        return item;
    }
    public Item getItemByName(String name){
        Item item = itemRepository.getItemByName(name)
                .orElseThrow(()-> new EntityNotFoundException("Item no encontrado"));
        return item;
    }

    public List<Item> filterItems(String name, String description) {
        List<Item> results = itemRepository.searchAllByQ(name,description);
        if(results.isEmpty())
            throw new EntityNotFoundException("No se encontraron items");
        return results;
    }
    @Transactional
    public void deleteItem(long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Transactional
    public Item updateItem(long id,SimpleItemDTO itemData) {
        Item storedItem = itemRepository.getItemById(id)
                .orElseThrow(()-> new EntityNotFoundException("No se ecnontró el item"));
        itemMapper.mergeChanges(storedItem,itemData);
        return itemRepository.save(storedItem);
    }

    public List<SimpleItemDTO> getItemsByContainerId(long containerId) {
        List<Item> result = itemRepository.getItemByContainerId(containerId);
        return itemMapper.toSimpleItemDtoList(result);
    }
}
