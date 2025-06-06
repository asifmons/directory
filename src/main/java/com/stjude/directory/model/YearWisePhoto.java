package com.stjude.directory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "YEAR_WISE_PHOTOS")
public class YearWisePhoto {

    @Id
    private String id;

    private int year;
    private String url;
    private String fileName;
    private LocalDateTime uploadedAt;

    public YearWisePhoto() {
    }

    public YearWisePhoto(int year, String url, String fileName, LocalDateTime uploadedAt) {
        this.year = year;
        this.url = url;
        this.fileName = fileName;
        this.uploadedAt = uploadedAt;
    }

    public String getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }



    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
