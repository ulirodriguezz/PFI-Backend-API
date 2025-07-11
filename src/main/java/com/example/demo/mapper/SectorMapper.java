package com.example.demo.mapper;

import com.example.demo.dto.SectorDTO;
import com.example.demo.model.Sector;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SectorMapper {
    public Sector toSectorEntity(SectorDTO dto){
        Sector sector = new Sector();
        sector.setDescription(dto.getDescription());
        sector.setName(dto.getName());
        return sector;
    }
    public SectorDTO toSectorDTO(Sector sector){
        SectorDTO dto = new SectorDTO();
        dto.setId(sector.getId());
        dto.setDescription(sector.getDescription());
        dto.setName(sector.getName());
        return dto;
    }

    public List<SectorDTO> toSectorDtoList(List<Sector> sectorList){
        List<SectorDTO> dtos = new ArrayList<>();
        for(Sector s : sectorList){
            dtos.add(this.toSectorDTO(s));
        }
        return dtos;
    }
}
