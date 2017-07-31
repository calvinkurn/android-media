package com.tokopedia.seller.common.williamchart.util;

import com.tokopedia.seller.common.williamchart.renderer.StringFormatRenderer;

public class YAxisRenderer implements StringFormatRenderer {
    @Override
    public String formatString(String s) {
        return KMNumbers.formatNumbers(Long.valueOf(s));
    }
}
