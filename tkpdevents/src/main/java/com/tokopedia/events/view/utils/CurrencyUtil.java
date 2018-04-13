package com.tokopedia.events.view.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by pranaymohapatra on 06/12/17.
 */

public class CurrencyUtil {
    public static Locale locale = new Locale("in", "ID");
    public static final String RUPIAH_FORMAT = "Rp %s";
    public static String convertToCurrencyString(Integer value){
        return NumberFormat.getNumberInstance(locale).format(value.longValue());
    }
}
