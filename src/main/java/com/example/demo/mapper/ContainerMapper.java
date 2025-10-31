package com.example.demo.mapper;

import com.example.demo.dto.ContainerPreviewDTO;
import com.example.demo.dto.FullContainerDTO;
import com.example.demo.dto.SimpleContainerDTO;
import com.example.demo.model.Container;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContainerMapper {

    private final SectorMapper sectorMapper;

    public ContainerMapper(SectorMapper sectorMapper) {
        this.sectorMapper = sectorMapper;
    }

    public SimpleContainerDTO toSimpleDTO(Container container){
        SimpleContainerDTO dto = new SimpleContainerDTO();
        dto.setId(container.getId());
        dto.setName(container.getName());
        dto.setDescription(container.getDescription());
        dto.setTagId(container.getRfidTag());
        if(container.getSector() != null)
            dto.setSectorId(container.getSector().getId());
        dto.setReaderId(container.getReaderId());
        return dto;
    }
    public FullContainerDTO toFullContainerDTO(Container container){
        FullContainerDTO dto = new FullContainerDTO();
        dto.setId(container.getId());
        dto.setName(container.getName());
        dto.setDescription(container.getDescription());
        dto.setSectorInfo(sectorMapper.toSimpleSectorDTO(container.getSector()));
        if(container.getSector() != null)
            dto.setSectorId(container.getSector().getId());
        dto.setReaderId(container.getReaderId());
        dto.setTagId(container.getRfidTag());
        return dto;
    }
    public Container toContainerEntity(SimpleContainerDTO dto){
        Container container = new Container();
        container.setName(dto.getName());
        container.setDescription(dto.getDescription());
        container.setRfidTag(dto.getTagId());
        return container;
    }
    public void mergeChanges(Container storedContainer, SimpleContainerDTO changedData){
        if(changedData.getDescription() != null)
            storedContainer.setDescription(changedData.getDescription());
        if(changedData.getName() != null)
            storedContainer.setName(changedData.getName());
        if(changedData.getReaderId() != null)
            storedContainer.setReaderId(changedData.getReaderId());
        if(changedData.getTagId() != null)
            storedContainer.setRfidTag(changedData.getTagId());
    }
    public List<SimpleContainerDTO> toSimpleDTOList(List<Container> containers){
        List<SimpleContainerDTO> dtos = new ArrayList<>();
        for(Container c : containers){
            dtos.add(this.toSimpleDTO(c));
        }
        return dtos;
    }
    public ContainerPreviewDTO toContainerPreviewDTO(Container container){
        ContainerPreviewDTO dto = new ContainerPreviewDTO();
        dto.setId(container.getId());
        dto.setName(container.getName());

        return dto;
    }

    public List<ContainerPreviewDTO> toContainerPreviewList(List<Container> containers){
        List<ContainerPreviewDTO> dtos = new ArrayList<>();

        for(Container c : containers){
            dtos.add(this.toContainerPreviewDTO(c));
        }

        return dtos;
    }

}
