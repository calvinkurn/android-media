package com.tokopedia.seller.common.williamchart.util;

import com.tokopedia.seller.goldmerchant.statistic.view.widget.config.GrossGraphChartConfig;
import com.tokopedia.seller.common.williamchart.Tools;
import com.tokopedia.seller.common.williamchart.renderer.StringFormatRenderer;

/**
 * Created by normansyahputa on 7/7/17.
 */

public class TopAdsBaseWilliamChartConfig extends GrossGraphChartConfig {
    public static final int HEIGHT_TIP = 80;
    public static final int WIDTH_TIP = 60;

    @Override
    public StringFormatRenderer yStringFormatRenderer() {
        return new TopAdsYAxisRenderer();
    }

    @Override
    public int topMargin() {
        return (int) Tools.fromDpToPx(WIDTH_TIP);
    }
}
