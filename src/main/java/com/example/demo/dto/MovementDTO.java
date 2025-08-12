package com.example.demo.dto;

import java.time.Instant;
import java.time.LocalDateTime;

public class MovementDTO {

    private long id;
    private String tagId;
    private String containerReaderId;
    private Instant timestamp;

    public MovementDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getContainerReaderId() {
        return containerReaderId;
    }

    public void setContainerReaderId(String containerReaderId) {
        this.containerReaderId = containerReaderId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
