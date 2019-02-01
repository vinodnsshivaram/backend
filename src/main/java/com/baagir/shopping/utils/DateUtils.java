package com.baagir.shopping.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static String getFormatedDate() {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat.setLenient(false);
        return dateFormat.format(new Date());
    }

    public static String getFormatedDate(Date date) {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat.setLenient(false);
        return dateFormat.format(date);
    }
}
