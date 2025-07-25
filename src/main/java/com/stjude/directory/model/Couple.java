package com.stjude.directory.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Couple {

    private String spouce1;
    private String spouce2;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date anniversaryDate;
    private Short coupleNo;
    private String spouse1Id;
    private String spouse2Id;
}
