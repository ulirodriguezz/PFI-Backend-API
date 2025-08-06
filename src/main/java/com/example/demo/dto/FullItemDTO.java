package com.example.demo.dto;

public class FullItemDTO extends SimpleItemDTO{
    boolean isFavorite;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
