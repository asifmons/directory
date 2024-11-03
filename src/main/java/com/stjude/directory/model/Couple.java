package com.stjude.directory.model;

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
    private Date anniversaryDate;
}
