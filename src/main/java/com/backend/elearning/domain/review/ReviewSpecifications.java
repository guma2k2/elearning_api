package com.backend.elearning.domain.review;

import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecifications {

    public static Specification<Review> contentContains(String content) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("content")), "%" + content.toLowerCase()+"%");
    }

    public static Specification<Review> hasStatus(ReviewStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
