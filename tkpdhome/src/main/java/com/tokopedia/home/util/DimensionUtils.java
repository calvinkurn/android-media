package com.tokopedia.home.util;

/**
 * Created by errysuprayogi on 3/16/18.
 */

import android.content.Context;
import android.util.TypedValue;

public class DimensionUtils {

    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue
                .applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dp,
                        context.getResources().getDisplayMetrics()
                );
    }
}