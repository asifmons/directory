package com.stjude.directory.model;

import com.stjude.directory.enums.Operation;
import lombok.Data;

import java.util.List;

@Data
public class FieldFilter implements Node {
    String fieldName;
    Operation operation;
    List<?> values;
}
