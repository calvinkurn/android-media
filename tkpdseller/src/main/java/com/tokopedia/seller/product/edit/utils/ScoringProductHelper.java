package com.tokopedia.seller.product.edit.utils;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;

import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 4/17/17.
 */

public class ScoringProductHelper {

    public static final String LOW_SCORE = "low-score";
    public static final String HIGH_SCORE = "high-score";
    public static final String MEDIUM_SCORE = "medium-score";

    @ColorInt
    public static int getColorOfScore(String color, Context context) {
        switch (color){
            case LOW_SCORE:
                return ContextCompat.getColor(context, R.color.low_score_value);
            case HIGH_SCORE:
                return ContextCompat.getColor(context, R.color.high_score_value);
            case MEDIUM_SCORE:
                return ContextCompat.getColor(context, R.color.medium_score_value);
            default:
                return ContextCompat.getColor(context, R.color.low_score_value);
        }
    }
}
