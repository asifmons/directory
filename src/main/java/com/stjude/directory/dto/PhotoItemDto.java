package com.stjude.directory.dto;

public class PhotoItemDto {

    private String id;
    private String url;

    public PhotoItemDto() {
    }

    public PhotoItemDto(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
