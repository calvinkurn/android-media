package com.tokopedia.flight.search.util;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.model.Duration;

/**
 * Created by User on 11/17/2017.
 */

public class DurationUtil {

    public static final int MINUTE_PER_DAY = 1440; // 60*24
    public static final int MINUTE_PER_HOUR = 60; // 60*24

    public static Duration convertFormMinute(int durationMinute){
        int day = durationMinute / MINUTE_PER_DAY;
        int durationModDay = durationMinute - day * MINUTE_PER_DAY;
        int hour = durationModDay / MINUTE_PER_HOUR;
        int minute = durationModDay - (hour * MINUTE_PER_HOUR);
        return new Duration(day, hour, minute);
    }

    public static String getReadableString(Context context, Duration duration) {
        boolean useLongFormat = true;
        int day = duration.getDay();
        int hour = duration.getHour();
        int minute = duration.getMinute();
        if (day > 0 && hour > 0 && minute > 0) {
            useLongFormat = false;
        }
        String durationFormat = "";
        if (day > 0) {
            if (useLongFormat) {
                durationFormat += context.getString(R.string.flight_duration_ddd, day);
            } else {
                durationFormat += context.getString(R.string.flight_duration_dd, day);
            }
        }
        if (hour > 0) {
            if (!TextUtils.isEmpty(durationFormat)) {
                durationFormat += " ";
            }
            if (useLongFormat) {
                durationFormat += context.getString(R.string.flight_duration_hhh, hour);
            } else {
                durationFormat += context.getString(R.string.flight_duration_hh, hour);
            }
        }

        if (minute > 0) {
            if (!TextUtils.isEmpty(durationFormat)) {
                durationFormat += " ";
            }
            if (useLongFormat) {
                durationFormat += context.getString(R.string.flight_duration_mmm, minute);
            } else {
                durationFormat += context.getString(R.string.flight_duration_mm, minute);
            }
        }
        return durationFormat;
    }

}
