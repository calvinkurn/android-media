package com.tokopedia.tkpdstream.common.util;

import java.text.DecimalFormat;

/**
 * @author by StevenFredian on 05/03/18.
 */

public class TextFormatter {

    public static String format(long number){
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String temp = formatter.format(number);
        return temp.replace(",",".");
    }

    public static String format(String number) {
        long temp = Long.valueOf(number);
        return format(temp);
    }
}
