package com.example.demo.repository;

import com.example.demo.model.Container;
import com.example.demo.model.Image;
import com.example.demo.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {

    Optional<Image> getImageById(long id);
    List<Image> getAllByItemIdOrderByCreatedAtAsc(long itemId);

    List<Image> getAllByContainerIdOrderByCreatedAtAsc(long ItemId);

}
