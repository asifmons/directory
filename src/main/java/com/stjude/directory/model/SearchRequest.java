package com.stjude.directory.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {
    @Min(1)
    int pageSize;
    @Min(1)
    int offset;
    Node node;
    List<String> fields;
}
