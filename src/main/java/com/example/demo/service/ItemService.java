package com.example.demo.service;

import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    private ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
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
}
