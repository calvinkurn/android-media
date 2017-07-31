package com.tokopedia.seller.common.williamchart.util;


import com.tokopedia.seller.common.williamchart.renderer.StringFormatRenderer;

/**
 * Created by zulfikarrahman on 5/24/17.
 */

public class TopAdsYAxisRenderer implements StringFormatRenderer {
    @Override
    public String formatString(String rawString) {
        return KMNumbers.formatNumbersTopAds(Long.valueOf(rawString));
    }
}
