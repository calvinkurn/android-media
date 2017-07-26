/*
 * Copyright 2015 Diogo Bernardino
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tokopedia.seller.lib.williamchart;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.config.GrossGraphChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.config.GrossGraphDataSetConfig;
import com.tokopedia.seller.lib.williamchart.renderer.StringFormatRenderer;
import com.tokopedia.seller.lib.williamchart.renderer.XRenderer;
import com.tokopedia.seller.lib.williamchart.tooltip.Tooltip;
import com.tokopedia.seller.lib.williamchart.util.DefaultTooltipConfiguration;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

import java.util.List;


public class Tools {


    /**
     * Converts dp size into pixels.
     *
     * @param dp dp size to get converted
     * @return Pixel size
     */
    public static float fromDpToPx(float dp) {

        try {
            return dp * Resources.getSystem().getDisplayMetrics().density;
        } catch (Exception e) {
            return dp;
        }
    }


    /**
     * Converts a {@link Drawable} into {@link Bitmap}.
     *
     * @param drawable {@link Drawable} to be converted
     * @return {@link Bitmap} object
     */
    public static Bitmap drawableToBitmap(@NonNull Drawable drawable) {

        if (drawable instanceof BitmapDrawable) return ((BitmapDrawable) drawable).getBitmap();

        Bitmap bitmap =
                Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                        Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    /**
     * Find the Greatest Common Denominator.
     * https://en.wikipedia.org/wiki/Euclidean_algorithm
     *
     * @param min Mininum value
     * @param max Maximum value
     * @return Greatest common denominator
     */
    public static int GCD(int min, int max) {

        return max == 0 ? min : GCD(max, min % max);
    }


    /**
     * Finds the largest divisor of a number.
     *
     * @param num Value to be found the largest divisor
     * @return Largest divisor of parameter given
     */
    public static int largestDivisor(int num) {

        if (num > 1)
            for (int i = num / 2; i >= 0; i--)
                if (num % i == 0) {
                    return i;
                }
        return 1;
    }

    /**
     * Finds the largest divisor of a number.
     *
     * @param max Value to be found the largest divisor
     * @param min Value to be found the largest divisor
     * @return Largest divisor of parameter given
     */
    public static int largestDivisor(int max, int min) {
        int num = max - min;
        if (num > 1) {
            for (int i = num / 2; i > 0; i--) {
                if (num % i == 0 && (num / i) < 10) {
                    return i;
                }
            }
        }
        return 1;
    }

    public static BaseWilliamChartConfig getCommonWilliamChartConfig(Activity activity,
                                                                     LineChartView lineChartView,
                                                                     final BaseWilliamChartModel baseWilliamChartModel) {
        lineChartView.dismissAllTooltips();
        // resize linechart according to data
        GMStatisticUtil.resizeChart(baseWilliamChartModel.size(), lineChartView, activity);
        // get index to display
        final List<Integer> indexToDisplay = GMStatisticUtil.indexToDisplay(baseWilliamChartModel.getValues());
        Drawable oval2Copy6 = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.oval_2_copy_6, null);
        Tooltip tooltip = getTooltip(lineChartView.getContext(), getTooltipResLayout());
        BaseWilliamChartConfig baseWilliamChartConfig = new BaseWilliamChartConfig();
        baseWilliamChartConfig
                .reset()
                .addBaseWilliamChartModels(baseWilliamChartModel, new GrossGraphDataSetConfig())
                .setDotDrawable(oval2Copy6)
                .setBasicGraphConfiguration(new GrossGraphChartConfig())
                .setTooltip(tooltip, new DefaultTooltipConfiguration())
                .setxRendererListener(new XRenderer.XRendererListener() {
                    @Override
                    public boolean filterX(@IntRange(from = 0L) int i) {
                        if (i == 0 || baseWilliamChartModel.getValues().length - 1 == i)
                            return true;
                        if (baseWilliamChartModel.getValues().length <= 15) {
                            return true;
                        }
                        return indexToDisplay.contains(i);

                    }
                });
        return baseWilliamChartConfig;
    }

    private static Tooltip getTooltip(Context context, @LayoutRes int layoutRes) {
        return new Tooltip(context,
                layoutRes,
                R.id.gm_stat_tooltip_textview,
                new StringFormatRenderer() {
                    @Override
                    public String formatString(String s) {
                        return KMNumbers.formatNumbers(Float.valueOf(s));
                    }
                });
    }


    @LayoutRes
    private static int getTooltipResLayout() {
        @LayoutRes int layoutTooltip = R.layout.gm_stat_tooltip_lollipop;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < android.os.Build.VERSION_CODES.LOLLIPOP) {
            layoutTooltip = R.layout.gm_stat_tooltip;
        }
        return layoutTooltip;
    }
}