package com.stjude.directory.model;

import com.stjude.directory.enums.EvaluationType;
import lombok.Data;

import java.util.List;
@Data
public class FilterCriteria implements Node {
    EvaluationType evaluationType;
    List<Node> filters;
}
