package com.tokopedia.gm.statistic.view.widget.config;

import com.tokopedia.seller.common.utils.KMNumbers;
import com.tokopedia.seller.common.williamchart.renderer.StringFormatRenderer;

/**
 * Created by sebastianuskh on 12/20/16.
 */

public class YAxisRenderer implements StringFormatRenderer {
    @Override
    public String formatString(String s) {
        return KMNumbers.formatSuffixNumbers(Long.valueOf(s));
    }
}
