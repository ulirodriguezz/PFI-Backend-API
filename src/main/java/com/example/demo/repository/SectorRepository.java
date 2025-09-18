package com.example.demo.repository;

import com.example.demo.model.Sector;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectorRepository extends JpaRepository<Sector,Long> {

    Optional<Sector> getSectorById(long sectorId);

    List<Sector> findAllByNameContainsOrDescriptionContaining(String name, String description);

    @Query("FROM Sector s WHERE s.tenant.id = :tenantId ")
    List<Sector> findSectorsByTenantId(@Param("tenantId") long tenantId);
}
