package com.tokopedia.flight.search.util;

import android.content.Context;

import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.model.Duration;

/**
 * Created by User on 11/17/2017.
 */

public class DurationUtil {

    public static final int MINUTE_PER_DAY = 1440; // 60*24
    public static final int MINUTE_PER_HOUR = 60; // 60*24

    public static Duration convertFormMinute(int durationMinute){
        int duration = durationMinute;
        int day = duration / MINUTE_PER_DAY;
        int durationModDay = duration - day * MINUTE_PER_DAY;
        int hour = durationModDay / MINUTE_PER_HOUR;
        int minute = durationModDay - (hour * MINUTE_PER_HOUR);
        return new Duration(day, hour, minute);
    }

}
