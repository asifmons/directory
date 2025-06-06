package com.stjude.directory.dto;

import java.util.List;

public class YearPhotoPreview {

    private int year;
    private List<PhotoItemDto> photos;

    public YearPhotoPreview() {
    }

    public YearPhotoPreview(int year, List<PhotoItemDto> photos) {
        this.year = year;
        this.photos = photos;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<PhotoItemDto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoItemDto> photos) {
        this.photos = photos;
    }
}
