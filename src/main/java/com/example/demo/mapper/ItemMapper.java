package com.example.demo.mapper;

import com.example.demo.dto.FullItemDTO;
import com.example.demo.dto.ItemPreviewDTO;
import com.example.demo.dto.SimpleItemDTO;
import com.example.demo.model.Item;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        if(item.getContainer() != null)
            dto.setContainerId(item.getContainer().getId());
        return dto;
    }

    public FullItemDTO toFullItemDTO(Item item,boolean isFavorite){
        FullItemDTO dto = new FullItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setLocationDescription(item.getLocationDescription());
        dto.setTagId(item.getTagId());
        if(item.getContainer() != null)
            dto.setContainerId(item.getContainer().getId());
        dto.setFavorite(isFavorite);
        return dto;
    }

    public List<SimpleItemDTO> toSimpleItemDtoList(List<Item> matchingItems) {
        List<SimpleItemDTO> dtos = new ArrayList<>();
        for(Item i : matchingItems){
            dtos.add(this.toSimpleItemDTO(i));
        }
        return dtos;
    }

    public ItemPreviewDTO toItemPreviewDTO(Item item){
        ItemPreviewDTO dto = new ItemPreviewDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        return dto;
    }

    public List<ItemPreviewDTO> toItemPreviewList(List<Item> items){
        List<ItemPreviewDTO> dtos = new ArrayList<>();
        for(Item i : items){
            dtos.add(this.toItemPreviewDTO(i));
        }
        return dtos;
    }

    public void mergeChanges(Item item, SimpleItemDTO dto){
        if(dto.getName() != null)
            item.setName(dto.getName());
        if(dto.getDescription() != null)
            item.setDescription(dto.getDescription());
        if(dto.getTagId() != null)
            item.setTagId(dto.getTagId());
        if(dto.getLocationDescription() != null)
            item.setLocationDescription(dto.getLocationDescription());
    }
}
