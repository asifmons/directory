package com.stjude.directory;

import com.stjude.directory.enums.EvaluationType;
import com.stjude.directory.model.FieldFilter;
import com.stjude.directory.model.FilterCriteria;
import com.stjude.directory.model.Node;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class CriteriaHelper {
    public static Criteria createCriteria(Node node) {
        Criteria criteria = new Criteria();
        return createCriteria(node, criteria);
    }

    private static Criteria createCriteria(Node node, Criteria criteria) {
        if (node instanceof FilterCriteria filterCriteria) {
            List<Criteria> criteriaList = filterCriteria.getFilters().stream()
                    .map(node1 -> createCriteria(node1, criteria)).toList();
            if (filterCriteria.getEvaluationType().equals(EvaluationType.AND)) {
                return criteria.andOperator(criteriaList);
            } else {
                return criteria.orOperator(criteriaList);
            }
        } else {
            FieldFilter fieldFilter = (FieldFilter) node;
            return getCriteriaBasedOnOperator(fieldFilter);
        }
    }

    private static Criteria getCriteriaBasedOnOperator(FieldFilter fieldFilter) {
        return switch (fieldFilter.getOperation()) {
            case STARTS_WITH -> Criteria.where(fieldFilter.getFieldName())
                    .regex("^" + fieldFilter.getValues().getFirst(), "i");
            case EQUALS -> Criteria.where(fieldFilter.getFieldName())
                    .is(fieldFilter.getValues().getFirst());
            case NOT_EQUALS -> Criteria.where(fieldFilter.getFieldName())
                    .ne(fieldFilter.getValues().getFirst());
            case GTE -> Criteria.where(fieldFilter.getFieldName())
                    .gte(fieldFilter.getValues().getFirst());
            case LTE -> Criteria.where(fieldFilter.getFieldName())
                    .lte(fieldFilter.getValues().getFirst());
            case LT -> Criteria.where(fieldFilter.getFieldName())
                    .lt(fieldFilter.getValues().getFirst());
            case GT -> Criteria.where(fieldFilter.getFieldName())
                    .gt(fieldFilter.getValues().getFirst());
        };
    }
}
