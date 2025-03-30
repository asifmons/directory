package com.stjude.directory;

import com.stjude.directory.enums.EvaluationType;
import com.stjude.directory.model.FieldFilter;
import com.stjude.directory.model.FilterCriteria;
import com.stjude.directory.model.Node;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class CriteriaHelper {

    public static Criteria createCriteria(Node node) {
        return node instanceof FilterCriteria ? buildCompositeCriteria((FilterCriteria) node) : buildFieldCriteria((FieldFilter) node);
    }

    private static Criteria buildCompositeCriteria(FilterCriteria filterCriteria) {
        List<Criteria> criteriaList = filterCriteria.getFilters().stream()
                .map(CriteriaHelper::createCriteria)
                .toList();
        if (criteriaList.isEmpty()) {
            return new Criteria();
        }

        return filterCriteria.getEvaluationType() == EvaluationType.AND
                ? new Criteria().andOperator(criteriaList)
                : new Criteria().orOperator(criteriaList);
    }

    private static Criteria buildFieldCriteria(FieldFilter fieldFilter) {
        Criteria criteria = Criteria.where(fieldFilter.getFieldName());
        Object value = fieldFilter.getValues().getFirst();

        return switch (fieldFilter.getOperation()) {
            case STARTS_WITH -> criteria.regex("^" + value, "i");
            case EQUALS -> criteria.is(value);
            case NOT_EQUALS -> criteria.ne(value);
            case GTE -> criteria.gte(value);
            case LTE -> criteria.lte(value);
            case LT -> criteria.lt(value);
            case GT -> criteria.gt(value);
        };
    }
}
