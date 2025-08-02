package com.example.demo.dto;

import java.util.List;

public class FullContainerDTO extends SimpleContainerDTO {
    private List<ItemPreviewDTO> items;
    private SimpleSectorDTO sectorInfo;

    public FullContainerDTO() {
    }

    public List<ItemPreviewDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemPreviewDTO> items) {
        this.items = items;
    }

    public SimpleSectorDTO getSectorInfo() {
        return sectorInfo;
    }

    public void setSectorInfo(SimpleSectorDTO sectorInfo) {
        this.sectorInfo = sectorInfo;
    }
}
