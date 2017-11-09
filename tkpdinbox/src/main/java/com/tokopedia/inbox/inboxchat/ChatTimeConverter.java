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

        String span = String.valueOf(DateUtils.getRelativeTimeSpanString(System.currentTimeMillis(), unixTime, 0));
        span = span.toLowerCase();

        Date postTime = new Date(unixTime);

        Calendar calPostDate = Calendar.getInstance();
        calPostDate.setTime(postTime);

        Calendar calCurrentTime = Calendar.getInstance();
        calCurrentTime.setTime(new Date());

        if (DateUtils.isToday(unixTime)) {
            return DateFormat.getTimeFormat(MainApplication.getAppContext()).format(unixTime);
        } else if (span.contains("yesterday")
                || (!DateUtils.isToday(unixTime) && span.contains("hours"))
                || (!DateUtils.isToday(unixTime) && span.contains("hours"))) {
            return YESTERDAY;
        } else if (calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR)) {
            SimpleDateFormat sdfDay = new SimpleDateFormat("dd MMM", localeID);
            return sdfDay.format(postTime);
        } else {
            SimpleDateFormat sdfYear = new SimpleDateFormat("dd MMM yyyy", localeID);
            return sdfYear.format(postTime);
        }
    }

    public static String formatTime(long unixTime) {
        return DateFormat.getTimeFormat(MainApplication.getAppContext()).format(unixTime);
    }

    private static String YESTERDAY = "Kemarin";
}
