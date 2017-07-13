package com.tokopedia.seller.selling.appwidget;

import com.tokopedia.core.R;

import android.support.annotation.ColorInt;
import android.support.annotation.IntegerRes;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

public class UtilsWidget {
    public static String getDaysLeftDeadlineOrder(int orderDayLeft){
        switch (orderDayLeft) {
            case 0:
                return "Hari ini";
            case 1:
                return "Besok";
            default:
                return orderDayLeft + " Hari Lagi";
        }
    }

    public static int getColorLeftDeadLineOrder(int orderDayLeft){
        switch (orderDayLeft) {
            case 0:
                return R.color.red_a700;
            case 1:
                return R.color.tkpd_status_orange;
            default:
                return R.color.tkpd_status_blue;
        }
    }
}
