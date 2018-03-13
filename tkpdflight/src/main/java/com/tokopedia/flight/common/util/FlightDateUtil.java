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
    public static final String FORMAT_DATE_API = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String FORMAT_TIME = "HH:mm";
    public static final String FORMAT_DATE = "EEEE, dd LLLL yyyy";
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_VIEW_FORMAT = "dd MMM yyyy";
    public static final String DEFAULT_VIEW_TIME_FORMAT = "dd MMM yyyy, HH:mm";
    public static final Locale DEFAULT_LOCALE = new Locale("in", "ID");
    public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("GMT+7");
    public static final String FORMAT_DATE_API_DETAIL = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String FORMAT_TIME_DETAIL = "HH:mm";
    public static final String FORMAT_DATE_LOCAL_DETAIL = "EEEE, dd LLLL yyyy";
    public static final String FORMAT_DATE_LOCAL_DETAIL_ORDER = "dd MMM yyyy, HH:mm";

    public static String formatDate(String currentFormat, String newFormat, String dateString) {
        return formatDate(currentFormat, newFormat, dateString, DEFAULT_LOCALE);
    }

    public static String formatDateByUsersTimezone(String currentFormat, String newFormat, String dateString) {
        TimeZone timeZone = TimeZone.getDefault();
        return formatDate(currentFormat, newFormat, dateString, DEFAULT_LOCALE, DEFAULT_TIMEZONE, timeZone);
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
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }

    }

    public static String formatDate(String currentFormat,
                                    String newFormat,
                                    String dateString,
                                    Locale locale,
                                    TimeZone fromTimeZone,
                                    TimeZone toTimezone) {
        try {
            DateFormat fromFormat = new SimpleDateFormat(currentFormat, locale);
            fromFormat.setTimeZone(fromTimeZone);
            fromFormat.setLenient(false);
            DateFormat toFormat = new SimpleDateFormat(newFormat, locale);
            toFormat.setLenient(false);
            toFormat.setTimeZone(toTimezone);
            Date date = fromFormat.parse(dateString);
            return toFormat.format(date);
        } catch (ParseException e) {
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

    public static Date getCurrentDate() {
        Calendar now = getCurrentCalendar();
        return now.getTime();
    }

    public static Calendar getCurrentCalendar() {
//        return new GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"));
        return Calendar.getInstance();
    }

    public static Date addTimeToCurrentDate(int field, int value) {
        Calendar now = getCurrentCalendar();
        now
                .add(field, value);
        return now.getTime();
    }

    public static Date addTimeToSpesificDate(Date date, int field, int value) {
        Calendar now = getCurrentCalendar();
        now.setTime(date);
        now.add(field, value);
        return now.getTime();
    }
}
