package com.example.demo.repository;

import com.example.demo.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement,Long> {
    List<Movement> getAllByItemIdOrderByTimestampDesc(long itemId);
    void deleteAllByItemId(long itemId);

}
