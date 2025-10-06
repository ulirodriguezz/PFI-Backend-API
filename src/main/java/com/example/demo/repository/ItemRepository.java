package com.example.demo.repository;

import com.example.demo.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    Optional<Item> getItemByName(String name);
    Optional<Item> getItemById(Long id);
    List<Item> getItemByContainerId(long containerId);
    Optional<Item> getByTagId (String tagId);
    @Query("SELECT i FROM Item i WHERE i.name LIKE %:name OR i.description LIKE %:name")
    List<Item> searchAllByQ(@Param("name") String name, @Param("description") String description);
    @Query("SELECT i FROM Item i WHERE i.name LIKE CONCAT('%', :query, '%')")
    List<Item> searchAllByNameLike(@Param("query") String name);
    void deleteById(long id);
    @Modifying
    @Query("UPDATE Item i SET i.container = null WHERE i.container.id = :containerId")
    public void clearContainerReferenceFromItems(@Param("containerId") long containerId);
    @Query("SELECT COUNT(i) > 0 FROM User u JOIN u.favoriteItems i WHERE u.username = :username AND i.id = :itemId")
    boolean isItemFavorite(@Param("username") String username, @Param("itemId") Long itemId);
    @Query("FROM Item i WHERE i.tenant.id = :tenantId ")
    List<Item> findItemsByTenantId(@Param("tenantId") long tenantId);
}
