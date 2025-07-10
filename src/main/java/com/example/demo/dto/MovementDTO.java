package com.example.demo.dto;

import java.time.LocalDateTime;

public class MovementDTO {

    private long id;
    private long itemId;
    private long containerId;
    private LocalDateTime timestamp;

    public MovementDTO() {
    }

    public long getItemId() {
        return itemId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getContainerId() {
        return containerId;
    }

    public void setContainerId(long containerId) {
        this.containerId = containerId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
