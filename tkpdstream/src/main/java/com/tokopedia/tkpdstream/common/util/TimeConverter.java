package com.tokopedia.tkpdstream.common.util;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author by nisie on 2/22/18.
 */

public class TimeConverter {

    private static final String FORMAT_HOUR = "HH.mm";
    private static final String TODAY = "Hari ini";
    private static final String YESTERDAY = "Kemarin";

    public static String convertToHourFormat(long unixSeconds) {
        Date date = new Date(unixSeconds);
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_HOUR, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }

    public static String convertToHeaderDisplay(long headerTime) {
        Locale localeID = new Locale("in", "ID");

        Date postTime = new Date(headerTime);

        Calendar calPostDate = Calendar.getInstance();
        calPostDate.setTime(postTime);

        Calendar calCurrentTime = Calendar.getInstance();
        calCurrentTime.setTime(new Date());

        if (DateUtils.isToday(headerTime)) {
            return TODAY;
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

}
