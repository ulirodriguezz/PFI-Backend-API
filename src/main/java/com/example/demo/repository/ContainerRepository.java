package com.example.demo.repository;

import com.example.demo.model.Container;
import com.example.demo.model.Item;
import com.example.demo.model.Sector;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ContainerRepository extends JpaRepository<Container,Long> {
    Optional<Container> getContainerById(long id);
    Optional<Container> getContainerByReaderId(String readerId);
    List<Container> getAllByNameContainingIgnoreCase(String name);
    void deleteById(long id);
    @Modifying
    @Query("UPDATE Container c SET c.sector.id = null WHERE c.sector.id = :sectorId")
    void clearSectorReferenceFromContainer(@Param("sectorId") long sectorId);
    List<Container> findAllByNameContainsOrDescriptionContaining(String name, String description);
    List<Container> getAllBySector(Sector sector);
    @Query("FROM Container c WHERE c.tenant.id = :tenantId ")
    List<Container> findContainersByTenantId(@Param("tenantId") long tenantId);
    @Modifying
    @Query("UPDATE Container c SET c.readerId = null WHERE c.readerId = :readerId")
    void clearReaderReferenceFromContainers(@Param("readerId") String readerId);
}
