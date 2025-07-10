package com.example.demo.repository;

import com.example.demo.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    public Optional<Item> getItemByName(String name);
    public Optional<Item> getItemById(Long id);
    public List<Item> getItemByContainerId(long containerId);
    @Query("SELECT i FROM Item i WHERE i.name LIKE %:name OR i.description LIKE %:name")
    public List<Item> searchAllByQ(@Param("name") String name, @Param("description") String description);
    public void deleteById(long id);
    @Modifying
    @Query("UPDATE Item i SET i.container = null WHERE i.container.id = :containerId")
    public void clearContainerReferenceFromItems(@Param("containerId") long containerId);
}
