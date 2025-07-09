package com.example.demo.dto;

import java.util.Set;

public class FullContainerDTO extends SimpleContainerDTO {
    private Set<SimpleItemDTO> items;

    public FullContainerDTO() {
    }
    public Set<SimpleItemDTO> getItems() {
        return items;
    }
    public void setItems(Set<SimpleItemDTO> items) {
        this.items = items;
    }
}
