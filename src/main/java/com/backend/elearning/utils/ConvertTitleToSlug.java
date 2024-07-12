package com.backend.elearning.utils;

public class ConvertTitleToSlug {


    public static String convertTitleToSlug (String title) {
        String[] splitTitles = title.trim().split("\\s+");
        String slug = "";
        for (int start = 0; start < splitTitles.length; start++) {
            if (start == splitTitles.length - 1) {
                slug+= splitTitles[start];
                break;
            }
            slug+= splitTitles[start] + "-";

        }
        return slug;
    }
}
