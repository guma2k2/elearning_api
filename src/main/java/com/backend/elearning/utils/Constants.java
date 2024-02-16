package com.backend.elearning.utils;

public final class Constants {

    public final class ERROR_CODE {

        public static final String USER_NOT_FOUND = "USER_NOT_FOUND";

        public static final String CATEGORY_NOT_FOUND = "CATEGORY_NOT_FOUND";
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
        public static final String DEFAULT_PAGE_SIZE = "5";
        public static final String DEFAULT_PAGE_NUMBER = "0";
    }
}
