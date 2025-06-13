package com.stjude.directory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Document(collection = "vicars")
public class Vicar {
    @Id
    private String id;
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private boolean isPresent;
    private String imageUrl;

    // Constructors
    public Vicar() {}

    public Vicar(String name, LocalDate startDate, LocalDate endDate, boolean isPresent, String imageUrl) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPresent = isPresent;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public boolean isPresent() { return isPresent; }
    public void setPresent(boolean present) { isPresent = present; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Override
    public String toString() {
        return "Vicar{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isPresent=" + isPresent +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}