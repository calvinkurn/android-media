package com.tokopedia.inbox.inboxchat;

import android.text.format.DateFormat;
import android.text.format.DateUtils;

import com.tokopedia.core.app.MainApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by stevenfredian on 9/19/17.
 */

public class ChatTimeConverter {

    public static Calendar unixToCalendar(long unixTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixTime);
        return calendar;
    }

    public static String formatTimeStamp(long unixTime) {
        Locale localeID = new Locale("in", "ID");

        Date postTime = new Date(unixTime);

        Calendar calPostDate = Calendar.getInstance();
        calPostDate.setTime(postTime);

        Calendar calCurrentTime = Calendar.getInstance();
        calCurrentTime.setTime(new Date());

        if (DateUtils.isToday(unixTime)) {
            SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm", localeID);
            return sdfHour.format(postTime);
        } else if (calCurrentTime.get(Calendar.MONTH) == calPostDate.get(Calendar.MONTH)
                && calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR)
                && isYesterday(calCurrentTime.get(Calendar.DAY_OF_MONTH), calPostDate.get(Calendar.DAY_OF_MONTH))) {
            return YESTERDAY;
        } else if (calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR)) {
            SimpleDateFormat sdfDay = new SimpleDateFormat("dd MMM", localeID);
            return sdfDay.format(postTime);
        } else {
            SimpleDateFormat sdfYear = new SimpleDateFormat("dd MMM yyyy", localeID);
            return sdfYear.format(postTime);
        }
    }

    private static boolean isYesterday(int currentDay, int postDay) {
        return currentDay - postDay == 1;
    }

    public static String formatTime(long unixTime) {
        Locale localeID = new Locale("in", "ID");
        Date postTime = new Date(unixTime);
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm", localeID);
        return sdfHour.format(postTime);
    }

    public static String formatFullTime(long unixTime) {
        Locale localeID = new Locale("in", "ID");

        Date postTime = new Date(unixTime);

        Calendar calPostDate = Calendar.getInstance();
        calPostDate.setTime(postTime);

        Calendar calCurrentTime = Calendar.getInstance();
        calCurrentTime.setTime(new Date());

        if (DateUtils.isToday(unixTime)) {
            SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm", localeID);
            return (String.format("%s, %s", TODAY, sdfHour.format(postTime)));
        } else if (calCurrentTime.get(Calendar.MONTH) == calPostDate.get(Calendar.MONTH)
                && calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR)
                && isYesterday(calCurrentTime.get(Calendar.DAY_OF_MONTH), calPostDate.get(Calendar.DAY_OF_MONTH))) {
            SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm", localeID);
            return (String.format("%s, %s", YESTERDAY, sdfHour.format(postTime)));
        } else if (calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR)) {
            SimpleDateFormat sdfDay = new SimpleDateFormat("dd MMM, HH:mm", localeID);
            return sdfDay.format(postTime);
        } else {
            SimpleDateFormat sdfYear = new SimpleDateFormat("dd MMM yyyy, HH:mm", localeID);
            return sdfYear.format(postTime);
        }
    }

    private static String TODAY = "Hari ini";
    private static String YESTERDAY = "Kemarin";
}
