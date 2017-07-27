package com.tokopedia.seller.transaction.neworder.common;

import com.tokopedia.core.R;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IntegerRes;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

public class UtilsWidget {

    public static String getDaysLeftDeadlineOrder(int orderDayLeft, Context context){
        switch (orderDayLeft) {
            case 0:
                return context.getString(com.tokopedia.seller.R.string.app_widget_label_today);
            case 1:
                return context.getString(com.tokopedia.seller.R.string.app_widget_label_tomorrow);
            default:
                return String.format(context.getString(com.tokopedia.seller.R.string.app_widget_label_left_day), orderDayLeft);
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
