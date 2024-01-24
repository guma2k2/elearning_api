package com.backend.elearning.exception;

import java.util.ArrayList;
import java.util.List;

public record ErrorVm(String statusCode, String title, String details, List<String> fieldErrors) {
    public ErrorVm(String statusCode, String title, String details) {
        this(statusCode, title, details, new ArrayList<>());
    }
}
