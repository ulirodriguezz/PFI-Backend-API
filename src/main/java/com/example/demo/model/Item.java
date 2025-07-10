package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_id",nullable = true)
    private String tagId;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description",nullable = false,length = 200)
    private String description;

    @Column(name = "location_description",nullable = true,length = 255)
    private String locationDescription;
    // Falta la parte de las fotos
    @ManyToOne()
    @JoinColumn(name = "container_id",nullable = true)
    private Container container;


    public Item() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }
}
