package com.tokopedia.seller.common.williamchart.util;

import com.tokopedia.seller.common.williamchart.Tools;
import com.tokopedia.seller.common.williamchart.config.GrossGraphChartConfig;

/**
 * Created by normansyahputa on 7/7/17.
 */

public class TopAdsBaseWilliamChartConfig extends GrossGraphChartConfig {
    public static final int HEIGHT_TIP = 60;
    public static final int WIDTH_TIP = 80;

    @Override
    public int topMargin() {
        return (int) Tools.fromDpToPx(WIDTH_TIP);
    }
}
