package com.tokopedia.seller.goldmerchant.statistic.view.widget.config;

import com.tokopedia.seller.goldmerchant.statistic.utils.KMNumbers;
import com.tokopedia.seller.lib.williamchart.renderer.StringFormatRenderer;

/**
 * Created by sebastianuskh on 12/20/16.
 */

public class YAxisRenderer implements StringFormatRenderer {
    @Override
    public String formatString(String s) {
        return KMNumbers.formatNumbers(Long.valueOf(s));
    }
}
