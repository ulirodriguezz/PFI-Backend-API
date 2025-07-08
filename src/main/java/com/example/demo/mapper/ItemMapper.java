package com.example.demo.mapper;

import com.example.demo.dto.SimpleItemDTO;
import com.example.demo.model.Item;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ItemMapper {

    public Item toItemEntity(SimpleItemDTO dto){
        Item item = new Item();
        item.setDescription(dto.getDescription());
        item.setName(dto.getName());
        item.setTagId(dto.getTagId());
        item.setLocationDescription(dto.getLocationDescription());
        return item;
    }

    public SimpleItemDTO toSimpleItemDTO(Item item){
        SimpleItemDTO dto = new SimpleItemDTO.Builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .locationDescription(item.getLocationDescription())
                .tagId(item.getTagId())
                .build();
        return dto;
    }

    public Set<SimpleItemDTO> toSimpleItemDtoSet(Set<Item> matchingItems) {
        Set<SimpleItemDTO> dtos = new HashSet<>();
        for(Item i : matchingItems){
            dtos.add(this.toSimpleItemDTO(i));
        }
        return dtos;
    }
}
