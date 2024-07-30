package com.backend.elearning.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeUtils {

    private static final String PATTERN_PAYMENT = "yyyyMMddHHmmss";

    private static final String PATTERN_NORMAL = "yyyy-MM-dd HH:mm:ss";

    private static final String MONTH_YEAR= "tháng %s năm %s";

    public static final String PAYMENT_TYPE = "PAYMENT";
    public static final String NORMAL_TYPE = "NORMAL";


    public static LocalDateTime convertStringToLocalDateTime(String localDateTimeString, String type) {
        String pattern = "";
        if (type.equals(PAYMENT_TYPE)){
            pattern = PATTERN_PAYMENT;
        }else if (type.equals(NORMAL_TYPE)) {
            pattern = PATTERN_NORMAL;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeString, formatter);
        return localDateTime;
    }

    public static String convertLocalDateTimeToMonthYearText(LocalDateTime localDateTime) {
        int month = localDateTime.getMonthValue() ;
        int year = localDateTime.getYear() ;
        String formattedDate = String.format(MONTH_YEAR, month, year);
        return formattedDate;
    }


    public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        return null;
    }
}
