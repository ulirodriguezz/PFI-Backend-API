package com.example.demo.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "movements")
public class Movement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "item_id",nullable = false)
    private Item item;
    @ManyToOne
    @JoinColumn(name = "container_id",nullable = false)
    private Container destinationContainer;
    @Column(nullable = false)
    @CreationTimestamp
    private Instant timestamp;

    public Movement() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Container getDestinationContainer() {
        return destinationContainer;
    }

    public void setDestinationContainer(Container destinationContainer) {
        this.destinationContainer = destinationContainer;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
