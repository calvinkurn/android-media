package com.tokopedia.flight.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightDateUtil {
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static final String DEFAULT_VIEW_FORMAT = "dd MMM yyyy";
    public static final Locale DEFAULT_LOCALE = new Locale("in", "ID");

    public static String formatDate(String currentFormat, String newFormat, String dateString) {
        return formatDate(currentFormat, newFormat, dateString, DEFAULT_LOCALE);
    }

    public static String formatToUi(String dateStr) {
        return formatDate(DEFAULT_FORMAT, DEFAULT_VIEW_FORMAT, dateStr);
    }
    public static String formatDate(String currentFormat, String newFormat, String dateString, Locale locale) {

        try {
            DateFormat fromFormat = new SimpleDateFormat(currentFormat, locale);
            fromFormat.setLenient(false);
            DateFormat toFormat = new SimpleDateFormat(newFormat, locale);
            toFormat.setLenient(false);
            Date date = fromFormat.parse(dateString);
            return toFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dateString;
        }

    }

    public static Date stringToDate(String input) {
        DateFormat fromFormat = new SimpleDateFormat(DEFAULT_FORMAT, DEFAULT_LOCALE);
        try {
            return fromFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Date doesnt valid (" + input + ")");
        }
    }

    public static Date stringToDate(String format, String input) {
        DateFormat fromFormat = new SimpleDateFormat(format, DEFAULT_LOCALE);
        try {
            return fromFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Date doesnt valid (" + input + ")");
        }
    }

    public static String dateToString(Date currentDate, String outputFormat) {
        DateFormat format = new SimpleDateFormat(outputFormat, DEFAULT_LOCALE);
        return format.format(currentDate);
    }

    public static Date addDate(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date addTimeToCurrentDate(int field, int value) {
        Calendar now = new GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"));
        now
                .add(field, value);
        return now.getTime();
    }
}
