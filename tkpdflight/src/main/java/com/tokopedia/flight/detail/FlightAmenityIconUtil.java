package com.tokopedia.flight.detail;

import android.support.annotation.DrawableRes;

import com.tokopedia.flight.R;

/**
 * Created by zulfikarrahman on 11/6/17.
 */

public class FlightAmenityIconUtil {

    @DrawableRes
    public static int getImageResource(String icon) {
        switch (icon){
            case "baggage":
                return R.drawable.ic_baggage;
            default:
                return R.drawable.ic_meals;
        }
    }
}
