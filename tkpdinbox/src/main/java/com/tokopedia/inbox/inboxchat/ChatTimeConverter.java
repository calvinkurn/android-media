package com.tokopedia.inbox.inboxchat;

import android.text.format.DateFormat;
import android.text.format.DateUtils;

import com.tokopedia.core.app.MainApplication;

import java.util.Calendar;
import java.util.Date;

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
        String span = String.valueOf(DateUtils.getRelativeTimeSpanString(System.currentTimeMillis(), unixTime, 0));
        span = span.toLowerCase();
        if (span.contains("minute") || span.contains("second")) {
            return DateFormat.getTimeFormat(MainApplication.getAppContext()).format(unixTime);
        } else if (span.contains("yesterday")) {
            return YESTERDAY;
        } else {
            return DateFormat.getLongDateFormat(MainApplication.getAppContext()).format(unixTime);
        }
    }

    public static String formatTime(long unixTime) {
        return DateFormat.getTimeFormat(MainApplication.getAppContext()).format(unixTime);
    }

    private static String YESTERDAY = "Kemarin";
}
