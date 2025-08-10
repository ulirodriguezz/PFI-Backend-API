package com.example.demo.mapper;

import com.example.demo.dto.ImageDTO;
import com.example.demo.model.Image;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImageMapper {

    public ImageDTO toImageDTO(Image image){
        ImageDTO dto = new ImageDTO();
        dto.setId(image.getId());
        dto.setUrl(image.getImageURL());
        return dto;
    }

    public List<ImageDTO> toImageDtoList(List<Image> images) {
        List<ImageDTO> dtos = new ArrayList<>();
        for(Image i : images){
            dtos.add(toImageDTO(i));
        }
        return dtos;
    }
}
