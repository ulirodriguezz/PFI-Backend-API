package com.example.demo.mapper;

import com.example.demo.dto.FullSectorDTO;
import com.example.demo.dto.SimpleSectorDTO;
import com.example.demo.model.Sector;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SectorMapper {
    public Sector toSectorEntity(SimpleSectorDTO dto){
        Sector sector = new Sector();
        sector.setDescription(dto.getDescription());
        sector.setName(dto.getName());
        return sector;
    }
    public SimpleSectorDTO toSimpleSectorDTO(Sector sector){
        if(sector == null)
            return null;
        SimpleSectorDTO dto = new SimpleSectorDTO();
        dto.setId(sector.getId());
        dto.setDescription(sector.getDescription());
        dto.setName(sector.getName());
        dto.setSectorId(sector.getId());
        return dto;
    }

    public FullSectorDTO toFullSectorDTO (Sector sector){
        if(sector == null)
            return null;
        FullSectorDTO dto = new FullSectorDTO();
        dto.setId(sector.getId());
        dto.setDescription(sector.getDescription());
        dto.setName(sector.getName());
        return dto;
    }

    public List<SimpleSectorDTO> toSimpleSectorDtoList(List<Sector> sectorList){
        List<SimpleSectorDTO> dtos = new ArrayList<>();
        for(Sector s : sectorList){
            dtos.add(this.toSimpleSectorDTO(s));
        }
        return dtos;
    }
}
