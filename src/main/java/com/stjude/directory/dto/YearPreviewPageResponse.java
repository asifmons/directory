package com.stjude.directory.dto;

import java.util.List;

public class YearPreviewPageResponse {

    private int page;
    private int size;
    private int totalYears;
    private List<YearPhotoPreview> previews;

    public YearPreviewPageResponse() {
    }

    public YearPreviewPageResponse(int page, int size, int totalYears, List<YearPhotoPreview> previews) {
        this.page = page;
        this.size = size;
        this.totalYears = totalYears;
        this.previews = previews;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalYears() {
        return totalYears;
    }

    public void setTotalYears(int totalYears) {
        this.totalYears = totalYears;
    }

    public List<YearPhotoPreview> getPreviews() {
        return previews;
    }

    public void setPreviews(List<YearPhotoPreview> previews) {
        this.previews = previews;
    }
}
