package com.tokopedia.seller.common.williamchart.util;

import com.tokopedia.seller.common.williamchart.Tools;

/**
 * Created by normansyahputa on 7/7/17.
 */

public class DefaultTooltipConfiguration implements TooltipConfiguration {

    @Override
    public int width() {
        return (int) Tools.fromDpToPx(21);
    }

    @Override
    public int height() {
        return (int) Tools.fromDpToPx(15);
    }
}
