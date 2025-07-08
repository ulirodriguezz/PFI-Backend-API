package com.example.demo.repository;

import com.example.demo.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long> {
    public Optional<Item> getItemByName(String name);
    public Optional<Item> getItemById(Long id);
}
