package com.stjude.directory.dto;

import java.util.List;
import com.stjude.directory.dto.PhotoItemDto;

public class PreviewPageResponse {
    private int page;
    private int size;
    private int total;
    private List<PhotoItemDto> photos;

    public PreviewPageResponse(int page, int size, int total, List<PhotoItemDto> photos) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.photos = photos;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public int getTotal() {
        return total;
    }

    public List<PhotoItemDto> getPhotos() {
        return photos;
    }
}
