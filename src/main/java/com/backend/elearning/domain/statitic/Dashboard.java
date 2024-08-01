package com.backend.elearning.domain.statitic;

public record Dashboard (
        long totalOrders,
        long totalReviews,
        long totalCourses,
        long totalStudents
) {
}
