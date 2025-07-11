package com.example.demo.dto;

import java.util.Set;

public class FullContainerDTO extends SimpleContainerDTO {
    private Set<SimpleItemDTO> items;
    private SectorDTO sectorInfo;

    public FullContainerDTO() {
    }
    public Set<SimpleItemDTO> getItems() {
        return items;
    }
    public void setItems(Set<SimpleItemDTO> items) {
        this.items = items;
    }

    public SectorDTO getSectorInfo() {
        return sectorInfo;
    }

    public void setSectorInfo(SectorDTO sectorInfo) {
        this.sectorInfo = sectorInfo;
    }
}
