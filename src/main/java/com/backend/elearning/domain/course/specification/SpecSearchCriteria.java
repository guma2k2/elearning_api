package com.backend.elearning.domain.course.specification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecSearchCriteria {
    private String key;
    private Object value;
    private SearchOperation operation;
    private boolean orPredicate;

    public SpecSearchCriteria(String key, Object value, SearchOperation operation) {
        super();
        this.key = key;
        this.value = value;
        this.operation = operation;
    }

    public SpecSearchCriteria(String orPredicate, String key, SearchOperation operation, Object value) {
        super();
        this.orPredicate = orPredicate != null && orPredicate.equals(SearchOperation.OR_PREDICATE_FLAG);
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SpecSearchCriteria(String key, String operation, String prefix, String value, String suffix) {
        SearchOperation searchOperation = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (searchOperation != null) {
            if (searchOperation.equals(SearchOperation.EQUALITY)) {
                boolean isStartWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                boolean isEndWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

                if (isStartWithAsterisk && isEndWithAsterisk) {
                    searchOperation = SearchOperation.CONTAINS;
                } else if (isStartWithAsterisk) {
                    searchOperation = SearchOperation.STARTS_WITH;
                } else if (isEndWithAsterisk) {
                    searchOperation = SearchOperation.ENDS_WITH;
                }
            }
        }
        this.key = key;
        this.value = value;
        this.operation = searchOperation;
    }
}
