package com.tokopedia.ride.common.ride.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alvarisi on 7/21/17.
 */

public class RideUtils {
    private static final String TIME_SERVER_FORMAT = "dd MMM, yyyy HH:mm";
    private static final String TIME_VIEW_FORMAT = "dd MMM yyyy HH:mm";
    // 17 Jul, 2017 00:29 -> 07 Jul 2017 14:55


    public static String convertTime(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_SERVER_FORMAT, Locale.US);
        Date dt;
        try {
            dt = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }
        SimpleDateFormat outputFormatter = new SimpleDateFormat(TIME_VIEW_FORMAT, Locale.US);
        return outputFormatter.format(dt);
    }
}
