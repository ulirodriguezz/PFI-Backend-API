package com.example.demo.repository;

import com.example.demo.model.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ContainerRepository extends JpaRepository<Container,Long> {
    Optional<Container> getContainerById(long id);
    Set<Container> getAllByNameContaining(String name);
    void deleteById(long id);
}
