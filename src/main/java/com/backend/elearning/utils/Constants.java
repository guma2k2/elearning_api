package com.backend.elearning.utils;

public final class Constants {

    public final class ERROR_CODE {

        public static final String USER_NOT_FOUND = "USER_NOT_FOUND";

        public static final String CATEGORY_NOT_FOUND = "CATEGORY_NOT_FOUND";
        public static final String QUESTION_NOT_FOUND = "QUESTION_NOT_FOUND";
        public static final String COURSE_NOT_FOUND = "COURSE_NOT_FOUND";
        public static final String LECTURE_NOT_FOUND = "LECTURE_NOT_FOUND";
        public static final String SECTION_NOT_FOUND = "SECTION_NOT_FOUND";
        public static final String QUIZ_NOT_FOUND = "QUIZ_NOT_FOUND";
        public static final String PARENT_CATEGORY_NOT_FOUND = "PARENT_CATEGORY_NOT_FOUND";
        public static final String PARENT_CATEGORY_CANNOT_BE_ITSELF = "PARENT_CATEGORY_CANNOT_BE_ITSELF";

        public static final String CATEGORY_NAME_DUPLICATED = "CATEGORY_NAME_DUPLICATED";
        public static final String TOPIC_NOT_FOUND = "TOPIC_NOT_FOUND";
        public static final String TOPIC_NAME_DUPLICATED = "TOPIC_NAME_DUPLICATED";
        public static final String COURSE_TITLE_DUPLICATED = "COURSE_TITLE_DUPLICATED";
        public static final String USER_EMAIL_DUPLICATED = "USER_EMAIL_DUPLICATED";
        public static final String LECTURE_SECTION_NOT_SAME = "LECTURE_SECTION_NOT_SAME";

    }

    public final class PageableConstant {
        public static final String DEFAULT_PAGE_SIZE = "10";
        public static final String DEFAULT_PAGE_NUMBER = "0";

        public static final String DEFAULT_SORT_DIR = "desc";
    }
}
