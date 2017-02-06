package com.tokopedia.seller.topads.view.library.util;

import com.tokopedia.seller.topads.view.library.williamchart.renderer.StringFormatRenderer;

import java.util.Map;

public class YAxisRenderer implements StringFormatRenderer {
    @Override
    public String formatString(String s) {
        return KMNumbers.formatNumbers(Long.valueOf(s));
    }
}
