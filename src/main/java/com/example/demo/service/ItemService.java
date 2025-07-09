package com.example.demo.service;

import com.example.demo.dto.SimpleItemDTO;
import com.example.demo.mapper.ItemMapper;
import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ItemService {
    private ItemRepository itemRepository;
    private ItemMapper itemMapper;
    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper){
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }
    @Transactional
    public Item saveItem(Item item){
        Item savedItem = this.itemRepository.save(item);
        return savedItem;
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

    public Set<Item> filterItems(String name, String description) {
        Set<Item> results = itemRepository.searchAllByQ(name,description);
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
}
