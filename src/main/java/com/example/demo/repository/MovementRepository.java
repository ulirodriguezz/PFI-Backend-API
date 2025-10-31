package com.example.demo.repository;

import com.example.demo.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement,Long> {
    List<Movement> getAllByItemIdOrderByTimestampDesc(long itemId);
    void deleteAllByItemId(long itemId);

    @Modifying
    @Query("update Movement m Set m.destinationContainer = null WHERE m.destinationContainer.id = :containerId")
    void removeAllReferencesToContainer(@Param("containerId") long containerId);

}
