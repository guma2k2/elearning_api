package com.backend.elearning.domain.common;

import java.util.List;

public  record PageableData<T>(
        int pageNum,
        int pageSize,
        int totalElements,
        int totalPages,
        List<T> content
) {
}
