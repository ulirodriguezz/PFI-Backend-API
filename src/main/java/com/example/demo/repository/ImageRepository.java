package com.example.demo.repository;

import com.example.demo.model.Container;
import com.example.demo.model.Image;
import com.example.demo.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> getImageById(long id);

    List<Image> getAllByItemIdOrderByCreatedAtAsc(long itemId);

    List<Image> getAllByContainerIdOrderByCreatedAtAsc(long ItemId);

    @Modifying
    @Query("DELETE FROM Image i Where i.container.id = :containerId ")
    void deleteAllByContainerId(@Param("containerId") long containerId);

    @Modifying
    @Query("DELETE FROM Image i Where i.item.id = :itemId ")
    void deleteAllByItemId(@Param("itemId") long itemId);

    @Modifying
    @Query("UPDATE Image i SET i.item = NULL WHERE i.item.id = :itemId")
    void deleteAllReferencesToItem(@Param("itemId") long itemId);
}
