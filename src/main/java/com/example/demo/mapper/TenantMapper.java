package com.example.demo.mapper;

import com.example.demo.dto.TenantMinimalDTO;
import com.example.demo.model.Tenant;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TenantMapper {

    public TenantMinimalDTO toMinimalDTO(Tenant entity){
        return new TenantMinimalDTO(entity.getName(),entity.getId());
    }

    public List<TenantMinimalDTO> toTenandMinimalDTOlist(List<Tenant> entities){
        return entities.stream().map(this::toMinimalDTO).toList();
    }
}
