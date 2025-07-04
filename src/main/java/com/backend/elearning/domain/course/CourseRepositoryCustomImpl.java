package com.backend.elearning.domain.course;

import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.review.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CourseRepositoryCustomImpl {


    @PersistenceContext
    private EntityManager entityManager;

    public Page<Course> findByMultiFilter(
            String title,
            Float ratingStar,
            String[] level,
            Boolean[] free,
            String categoryName,
            Integer topicId,
            Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // ----------- MAIN QUERY -------------
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        Root<Course> root = cq.from(Course.class);
        root.fetch("category", JoinType.INNER).fetch("parent", JoinType.LEFT);
        root.fetch("topic", JoinType.INNER);
        root.fetch("user", JoinType.INNER);
        cq.distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        // level IN (...)
        if (level != null && level.length > 0) {
            CriteriaBuilder.In<String> levelIn = cb.in(root.get("level"));
            for (String l : level) {
                levelIn.value(l);
            }
            predicates.add(levelIn);
        }

        // free IN (...)
        if (free != null && free.length > 0) {
            CriteriaBuilder.In<Boolean> freeIn = cb.in(root.get("free"));
            for (Boolean f : free) {
                freeIn.value(f);
            }
            predicates.add(freeIn);
        }

        if (title != null && !title.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        // categoryName = cat.name or parent.name
        if (categoryName != null) {
            Join<Course, Category> categoryJoin = root.join("category");
            Join<Category, Category> parentJoin = categoryJoin.join("parent", JoinType.LEFT);
            Predicate matchCat = cb.equal(categoryJoin.get("name"), categoryName);
            Predicate matchParent = cb.equal(parentJoin.get("name"), categoryName);
            predicates.add(cb.or(matchCat, matchParent));
        }

        // topicId
        if (topicId != null) {
            predicates.add(cb.equal(root.get("topic").get("id"), topicId));
        }

        // status = 'PUBLISHED'
        predicates.add(cb.equal(root.get("status"), "PUBLISHED"));

        // Subquery for avg rating
        if (ratingStar != null) {
            Subquery<Double> subquery = cq.subquery(Double.class);
            Root<Review> reviewRoot = subquery.from(Review.class);
            subquery.select(cb.avg(reviewRoot.get("ratingStar")));
            subquery.where(cb.equal(reviewRoot.get("course").get("id"), root.get("id")));
            predicates.add(cb.greaterThanOrEqualTo(cb.coalesce(subquery, 0d), ratingStar.doubleValue()));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        // ----------- EXECUTE MAIN QUERY -------------
        TypedQuery<Course> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Course> courses = query.getResultList();

        // ----------- COUNT QUERY -------------
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Course> countRoot = countQuery.from(Course.class);
        countQuery.select(cb.countDistinct(countRoot));

        List<Predicate> countPredicates = new ArrayList<>();

        // Copy same predicates (repeat manually to avoid fetch joins)
        if (level != null && level.length > 0) {
            CriteriaBuilder.In<String> levelIn = cb.in(countRoot.get("level"));
            for (String l : level) {
                levelIn.value(l);
            }
            countPredicates.add(levelIn);
        }

        if (title != null && !title.isBlank()) {
            countPredicates.add(cb.like(cb.lower(countRoot.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (free != null && free.length > 0) {
            CriteriaBuilder.In<Boolean> freeIn = cb.in(countRoot.get("free"));
            for (Boolean f : free) {
                freeIn.value(f);
            }
            countPredicates.add(freeIn);
        }

        if (categoryName != null) {
            Join<Course, Category> categoryJoin = countRoot.join("category");
            Join<Category, Category> parentJoin = categoryJoin.join("parent", JoinType.LEFT);
            Predicate matchCat = cb.equal(categoryJoin.get("name"), categoryName);
            Predicate matchParent = cb.equal(parentJoin.get("name"), categoryName);
            countPredicates.add(cb.or(matchCat, matchParent));
        }

        if (topicId != null) {
            countPredicates.add(cb.equal(countRoot.get("topic").get("id"), topicId));
        }

        countPredicates.add(cb.equal(countRoot.get("status"), "PUBLISHED"));

        if (ratingStar != null) {
            Subquery<Double> subquery = countQuery.subquery(Double.class);
            Root<Review> reviewRoot = subquery.from(Review.class);
            subquery.select(cb.avg(reviewRoot.get("ratingStar")));
            subquery.where(cb.equal(reviewRoot.get("course").get("id"), countRoot.get("id")));
            countPredicates.add(cb.greaterThanOrEqualTo(cb.coalesce(subquery, 0d), ratingStar.doubleValue()));
        }

        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(courses, pageable, total);
    }
}
