package com.example.demo.mapper;

import com.example.demo.dto.FullContainerDTO;
import com.example.demo.dto.SimpleContainerDTO;
import com.example.demo.model.Container;
import org.springframework.stereotype.Component;

@Component
public class ContainerMapper {

    public SimpleContainerDTO toSimpleDTO(Container container){
        SimpleContainerDTO dto = new SimpleContainerDTO();
        dto.setId(container.getId());
        dto.setName(container.getName());
        dto.setDescription(container.getDescription());
        return dto;
    }
    public Container toContainerEntity(SimpleContainerDTO dto){
        Container container = new Container();
        container.setName(dto.getName());
        container.setDescription(dto.getDescription());
        return container;
    }
    public void mergeChanges(Container storedContainer, SimpleContainerDTO changedData){
        if(changedData.getDescription() != null)
            storedContainer.setDescription(changedData.getDescription());
        if(changedData.getName() != null)
            storedContainer.setName(changedData.getName());
    }
}
