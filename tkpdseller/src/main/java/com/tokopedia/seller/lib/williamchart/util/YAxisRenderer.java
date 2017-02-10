package com.tokopedia.seller.lib.williamchart.util;

import com.tokopedia.seller.lib.williamchart.renderer.StringFormatRenderer;

public class YAxisRenderer implements StringFormatRenderer {
    @Override
    public String formatString(String s) {
        return KMNumbers.formatNumbers(Long.valueOf(s));
    }
}
