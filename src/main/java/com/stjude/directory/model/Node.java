package com.stjude.directory.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"  // This is a field that should indicate the type
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FieldFilter.class, name = "fieldFilter"),
        @JsonSubTypes.Type(value = FilterCriteria.class, name = "filterCriteria")
})
public interface Node {
}
