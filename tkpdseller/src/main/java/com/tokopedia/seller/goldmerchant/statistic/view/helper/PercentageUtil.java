package com.tokopedia.seller.goldmerchant.statistic.view.helper;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author normansyahputa on 7/11/17.
 *         <p>
 *         this class let developer use their own logic instead of built in logic.
 */
public interface PercentageUtil {
    void calculatePercentage(double percentage, ImageView ivArrowIcon, TextView tvPercentage);
}
