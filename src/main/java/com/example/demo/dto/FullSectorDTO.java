package com.example.demo.dto;

import java.util.List;

public class FullSectorDTO extends SimpleSectorDTO{
    List<ContainerPreviewDTO> containers;

    public List<ContainerPreviewDTO> getContainers() {
        return containers;
    }

    public void setContainers(List<ContainerPreviewDTO> containers) {
        this.containers = containers;
    }
}

