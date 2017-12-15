package com.tokopedia.seller.common.williamchart.util;

import android.util.Log;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.seller.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by normansyahputa on 1/19/17.
 */
public class GoldMerchantDateUtils {
    public static final String YYYY_M_MDD = "yyyyMMdd";
    private static final Locale locale = new Locale("in", "ID");
    private static final String TAG = "GoldMerchantDateUtils";

    public static String getDateWithYear(int date, String[] monthNames) {
        List<String> dateRaw = getDateRaw(date);
        String year = dateRaw.get(2);
        String month = dateRaw.get(1);
        month = monthNames[Integer.parseInt(month) - 1];

        String day = String.valueOf(Integer.valueOf(dateRaw.get(0)));
        return day + " " + month + " " + year;
    }

    public static String getDateWithoutYear(int date, String[] monthNames) {
        List<String> dateRaw = getDateRaw(date);
        String year = dateRaw.get(2);
        String month = dateRaw.get(1);
        month = monthNames[Integer.parseInt(month) - 1];

        String day = String.valueOf(Integer.valueOf(dateRaw.get(0)));

        return day + " " + month;
    }

    public static long getDateWithYear(int date) {
        List<String> dateRaw = getDateRaw(date);
        String year = dateRaw.get(2);
        String month = dateRaw.get(1);
        String day = String.valueOf(Integer.valueOf(dateRaw.get(0)));
        Calendar instance = Calendar.getInstance();
        instance.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));

        return instance.getTimeInMillis();
    }

    public static String getDateWithoutYear(String date, String[] monthNames) {
        List<String> dateRaw = getDateRaw(date);
        String year = dateRaw.get(2);
        String month = dateRaw.get(1);
        month = monthNames[Integer.parseInt(month) - 1];

        String day = String.valueOf(Integer.valueOf(dateRaw.get(0)));
        return day + " " + month;
    }

    public static String getDateWithYear(String date, String[] monthNames) {
        List<String> dateRaw = getDateRaw(date);
        String year = dateRaw.get(2);
        String month = dateRaw.get(1);
        month = monthNames[Integer.parseInt(month) - 1];
        String day = String.valueOf(Integer.valueOf(dateRaw.get(0)));
        return day + " " + month + " " + year;
    }

    public static String getDate(Integer date) {
        List<String> dateRaw = getDateRaw(date);
        String month = dateRaw.get(1);
        String day = dateRaw.get(0);
        return day + " " + month;
    }

    public static String getDateRaw(String label, String[] monthNames) {
        String[] split = label.split(" ");
        return split[0] + " " + monthNames[Integer.parseInt(split[1]) - 1];
    }

    private static List<String> getDateRaw(String s) {
        List<String> result = new ArrayList<>();
        String year = s.substring(0, 4);
        String month = s.substring(4, 6);
        String day = s.substring(6);
        result.add(day);
        result.add(month);
        result.add(year);
        return result;
    }

    private static List<String> getDateRaw(int date) {
        List<String> result = new ArrayList<>();
        String s = Integer.toString(date);
        String year = s.substring(0, 4);
        String month = s.substring(4, 6);
        String day = s.substring(6);
        result.add(day);
        result.add(month);
        result.add(year);
        return result;
    }

    public static String getDateFormat(long timeInMillis) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timeInMillis);
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);
        return dateFormat.format(instance.getTime());
    }

    public static String getDateFormatForInput(long timeInMillis) {
        return getDateFormatForInput(timeInMillis, YYYY_M_MDD);
    }

    public static String getDateFormatForInput(long timeInMillis, String format) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timeInMillis);
        DateFormat dateFormat = new SimpleDateFormat(format, locale);
        return dateFormat.format(instance.getTime());
    }

    public static List<Integer> generateDateRanges(long sDate, long eDate){
        // calculate range
        Calendar sCDate = Calendar.getInstance();
        sCDate.setTimeInMillis(sDate);
        Calendar eCDate = Calendar.getInstance();
        eCDate.setTimeInMillis(eDate);
        // create list integer
        List<Integer> result = new ArrayList<>();
        int diffDays = daysBetween(eCDate, sCDate);
        result.add(Integer.parseInt(getDateFormatForInput(sCDate.getTimeInMillis())));
        for(int i=1;i<=diffDays;i++){
            sCDate.add(Calendar.DATE, 1);
            result.add(Integer.parseInt(getDateFormatForInput(sCDate.getTimeInMillis())));
        }
        return result;
    }

    public static String getPreviousDate(long currentDay, int previousDayCount, String format) {
        return getDateFormatForInput(getPreviousDate(currentDay, previousDayCount), format);
    }

    public static long getPreviousDate(long currentDay, int previousDayCount) {
        // calculate range
        Calendar sCDate = Calendar.getInstance();
        sCDate.setTimeInMillis(currentDay);
        sCDate.add(Calendar.DATE, -1 * (previousDayCount));
        return sCDate.getTimeInMillis();
    }

    public static Date getPreviousDateInDate(long currentDay, int previousDayCount) {
        // calculate range
        Calendar sCDate = Calendar.getInstance();
        sCDate.setTimeInMillis(currentDay);
        sCDate.add(Calendar.DATE, -1 * (previousDayCount));
        return new Date(sCDate.getTimeInMillis());
    }

    public static int daysBetween(Calendar day1, Calendar day2){
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays ;
        }
    }
}
