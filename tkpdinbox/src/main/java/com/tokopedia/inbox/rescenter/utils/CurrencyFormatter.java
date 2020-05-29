package com.tokopedia.inbox.rescenter.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by sebastianuskh on 12/5/16.
 */

public class CurrencyFormatter {

    static private NumberFormat formatRupiah = new DecimalFormat("#,###,###");

    public static String formatDotRupiah(String string) {
        if(string.isEmpty()) return "0";
        return formatRupiah.format(Double.valueOf(getRawString(string))).replaceAll(",",".");
    }

    public static String getRawString(String string) {
        return string.replace(",", "");
    }
}
