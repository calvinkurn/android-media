package com.tokopedia.seller.common.williamchart.util;

import com.tokopedia.seller.common.williamchart.Tools;

/**
 * Created by normansyahputa on 7/7/17.
 */

public class DefaultTooltipConfiguration implements TooltipConfiguration {

    public static final int DEFAULT_WIDTH = 38;
    public static final int DEFAULT_HEIGHT = 20;

    @Override
    public int width() {
        return (int) Tools.fromDpToPx(DEFAULT_WIDTH);
    }

    @Override
    public int height() {
        return (int) Tools.fromDpToPx(DEFAULT_HEIGHT);
    }
}
