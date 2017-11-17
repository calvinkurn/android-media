package com.tokopedia.flight.search.view.model;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.flight.R;

/**
 * Created by User on 11/17/2017.
 */

public class Duration {
    int day;
    int hour;
    int minute;

    public Duration(int day, int hour, int minute) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public String getReadableString(Context context) {
        boolean useLongFormat = true;
        if (day > 0 && hour > 0 && minute > 0) {
            useLongFormat = false;
        }
        String durationFormat = "";
        if (day > 0) {
            if (useLongFormat) {
                durationFormat += context.getString(R.string.duration_flight_ddd, day);
            } else {
                durationFormat += context.getString(R.string.duration_flight_dd, day);
            }
        }
        if (hour > 0) {
            if (!TextUtils.isEmpty(durationFormat)) {
                durationFormat += " ";
            }
            if (useLongFormat) {
                durationFormat += context.getString(R.string.duration_flight_hhh, hour);
            } else {
                durationFormat += context.getString(R.string.duration_flight_hh, hour);
            }
        }

        if (minute > 0) {
            if (!TextUtils.isEmpty(durationFormat)) {
                durationFormat += " ";
            }
            if (useLongFormat) {
                durationFormat += context.getString(R.string.duration_flight_mmm, minute);
            } else {
                durationFormat += context.getString(R.string.duration_flight_mm, minute);
            }
        }
        return durationFormat;
    }
}
