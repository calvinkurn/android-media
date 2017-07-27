package com.tokopedia.transaction.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by kris on 6/2/17. Tokopedia
 */

public class ValueConverter {

    public static String getStringIdrFormat(int value) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        kursIndonesia.setMaximumFractionDigits(0);
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setGroupingSeparator('.');
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setDecimalSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);

        return kursIndonesia.format(value).replace(",", ".");
    }

}
