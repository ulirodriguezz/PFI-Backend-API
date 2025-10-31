package com.example.demo.service;

import com.example.demo.dto.TenantMinimalDTO;
import com.example.demo.mapper.TenantMapper;
import com.example.demo.repository.TenantRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantService {
    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;


    public TenantService(TenantRepository tenantRepository, TenantMapper tenantMapper) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
    }

    @Transactional
    public List<TenantMinimalDTO> findAllTenants(){
        return tenantMapper.toTenandMinimalDTOlist(tenantRepository.findAll());
    }
}
