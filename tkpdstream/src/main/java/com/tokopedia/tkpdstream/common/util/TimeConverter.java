package com.tokopedia.tkpdstream.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author by nisie on 2/22/18.
 */

public class TimeConverter {

    private static final String FORMAT_HOUR = "HH.mm";

    public static String convertToHourFormat(long unixSeconds) {
        Date date = new Date(unixSeconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_HOUR, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }
}
