package com.stjude.directory.utils;



import com.stjude.directory.enums.EvaluationType;
import com.stjude.directory.model.FieldFilter;
import com.stjude.directory.model.FilterCriteria;
import com.stjude.directory.model.Node;
import org.bson.Document;

import java.util.List;
import java.util.ArrayList;

public class AtlasSearchHelper {

    public static Document createSearchQuery(Node node) {
        Document searchStage = new Document();
        Document searchQuery = node instanceof FilterCriteria
                ? buildCompositeCriteria((FilterCriteria) node)
                : buildFieldCriteria((FieldFilter) node);

        searchStage.put("$search", searchQuery);
        return searchStage;
    }

    private static Document buildCompositeCriteria(FilterCriteria filterCriteria) {
        List<Document> criteriaList = filterCriteria.getFilters().stream()
                .map(AtlasSearchHelper::buildSearchCriteria)
                .toList();

        if (criteriaList.isEmpty()) {
            return new Document("matchAll", new Document());
        }


        if (filterCriteria.getEvaluationType() == EvaluationType.AND) {
            return new Document("compound", new Document("must", criteriaList));
        } else {
            return new Document("compound", new Document("should", criteriaList)
                    .append("minimumShouldMatch", 1));
        }
    }

    private static Document buildSearchCriteria(Node node) {
        return node instanceof FilterCriteria
                ? buildCompositeCriteria((FilterCriteria) node)
                : buildFieldCriteria((FieldFilter) node);
    }

    private static Document buildFieldCriteria(FieldFilter fieldFilter) {
        String fieldName = fieldFilter.getFieldName();
        Object value = fieldFilter.getValues().get(0);

        return switch (fieldFilter.getOperation()) {
            case STARTS_WITH -> buildAutocompleteQuery(fieldName, value.toString());
            case EQUALS -> buildEqualsQuery(fieldName, value);
            case NOT_EQUALS -> buildNotEqualsQuery(fieldName, value);
            case GTE -> buildRangeQuery(fieldName, value, null, true, false);
            case LTE -> buildRangeQuery(fieldName, null, value, false, true);
            case LT -> buildRangeQuery(fieldName, null, value, false, false);
            case GT -> buildRangeQuery(fieldName, value, null, false, false);
        };
    }

    private static Document buildAutocompleteQuery(String fieldName, String value) {
        // Use autocomplete for name field, text search for others
        if ("name".equals(fieldName)) {
            return new Document("autocomplete", new Document()
                    .append("query", value)
                    .append("path", fieldName));
        } else {
            return new Document("text", new Document()
                    .append("query", value + "*")
                    .append("path", fieldName));
        }
    }

    private static Document buildEqualsQuery(String fieldName, Object value) {
        // Use appropriate operator based on field type from your index
        if (isTokenField(fieldName)) {
            return new Document("equals", new Document()
                    .append("path", fieldName)
                    .append("value", value.toString().toLowerCase()));
        } else if (isNumberField(fieldName)) {
            return new Document("equals", new Document()
                    .append("path", fieldName)
                    .append("value", value));
        } else if (isBooleanField(fieldName)) {
            return new Document("equals", new Document()
                    .append("path", fieldName)
                    .append("value", value));
        } else if (isDateField(fieldName)) {
            return new Document("equals", new Document()
                    .append("path", fieldName)
                    .append("value", value));
        } else {
            // Default to text search for exact match
            return new Document("text", new Document()
                    .append("query", "\"" + value + "\"")
                    .append("path", fieldName));
        }
    }

    private static Document buildNotEqualsQuery(String fieldName, Object value) {
        // Atlas Search doesn't have direct NOT EQUALS, so we use compound with mustNot
        Document equalsQuery = buildEqualsQuery(fieldName, value);
        return new Document("compound", new Document()
                .append("mustNot", List.of(equalsQuery)));
    }

    private static Document buildRangeQuery(String fieldName, Object gte, Object lte, boolean includeGte, boolean includeLte) {
        Document rangeDoc = new Document("path", fieldName);

        if (gte != null) {
            rangeDoc.append(includeGte ? "gte" : "gt", gte);
        }
        if (lte != null) {
            rangeDoc.append(includeLte ? "lte" : "lt", lte);
        }

        return new Document("range", rangeDoc);
    }

    // Helper methods to determine field types based on your index mapping
    private static boolean isTokenField(String fieldName) {
        return List.of("_id", "address", "bloodGroup", "emailId", "familyId", "phoneNumber", "status", "unit")
                .contains(fieldName);
    }

    private static boolean isNumberField(String fieldName) {
        return List.of("coupleNo").contains(fieldName);
    }

    private static boolean isBooleanField(String fieldName) {
        return List.of("isFamilyHead").contains(fieldName);
    }

    private static boolean isDateField(String fieldName) {
        return List.of("dob", "expiryDate").contains(fieldName);
    }

    // Utility method to create the complete aggregation pipeline
    public static List<Document> createSearchPipeline(Node node, Integer limit, Integer skip) {
        List<Document> pipeline = new ArrayList<>();

        // Add search stage
        pipeline.add(createSearchQuery(node));

        // Add skip stage if specified
        if (skip != null && skip > 0) {
            pipeline.add(new Document("$skip", skip));
        }

        // Add limit stage if specified
        if (limit != null && limit > 0) {
            pipeline.add(new Document("$limit", limit));
        }


        return pipeline;
    }
}