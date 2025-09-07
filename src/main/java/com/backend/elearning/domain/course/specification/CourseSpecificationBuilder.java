package com.backend.elearning.domain.course.specification;


import com.backend.elearning.domain.course.Course;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static com.backend.elearning.domain.course.specification.SearchOperation.*;

public class CourseSpecificationBuilder {

    public final List<SpecSearchCriteria> params;

    public CourseSpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public CourseSpecificationBuilder with (String key, String operation, Object value, String prefix, String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public CourseSpecificationBuilder with(String orPredicate, String key, String operation, Object value, String prefix, String suffix) {
        SearchOperation searchOperation = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (searchOperation != null) {
            if (searchOperation == EQUALITY) { // the operation may be complex operation
                final boolean startWithAsterisk = prefix != null && prefix.contains(ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    searchOperation = CONTAINS;
                } else if (startWithAsterisk) {
                    searchOperation = ENDS_WITH;
                } else if (endWithAsterisk) {
                    searchOperation = STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(orPredicate, key, searchOperation, value));
        }
        return this;
    }

    public Specification<Course> build() {
        if (params.isEmpty()) {
            return null;
        }

        Specification<Course> result = new CourseSpecification(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new CourseSpecification(params.get(i)))
                    : Specification.where(result).and(new CourseSpecification(params.get(i)));
        }
        return result;
    }

    public CourseSpecificationBuilder with(CourseSpecification specification) {
        params.add(specification.getCriteria());
        return this;
    }

    public CourseSpecificationBuilder with(SpecSearchCriteria criteria) {
        params.add(criteria);
        return this;
    }
}
