package com.murat.invoice.generation.utils;

import com.murat.invoice.generation.constants.ApplicationConstants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ApplicationUtils {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(ApplicationConstants.DATE_FORMAT);
    private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD = DateTimeFormatter.ofPattern(ApplicationConstants.DATE_FORMAT_YYYY_MM_DD);

    public static String getStringFromLocalDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate getLocalDate(String date){
        return LocalDate.parse(date,DATE_TIME_FORMATTER_YYYY_MM_DD);
    }

    public static String getLocalDate(LocalDate localDate){
        return localDate.format(DATE_TIME_FORMATTER_YYYY_MM_DD);
    }
}
