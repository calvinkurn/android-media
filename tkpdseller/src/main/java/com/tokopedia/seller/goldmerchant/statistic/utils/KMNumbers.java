package com.tokopedia.seller.goldmerchant.statistic.utils;

import android.content.Context;

import com.tokopedia.seller.R;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by normansyahputa on 12/16/16.
 */
public class KMNumbers {

    public static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    public static final String FORMAT_DOUBLE = "%.1f";
    public static final String FORMAT_2_DOUBLE = "%.2f";
    public static final String FORMAT = "%.1f%s";
    private static final Locale locale = new Locale("in", "ID");
    public static NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);

    static {
        suffixes.put(1000L, "rb");
        suffixes.put(1000000L, "jt");
        suffixes.put(1000000000L, "M");
        suffixes.put(1000000000000L, "T");
        suffixes.put(1000000000000000L, "B");
    }

    public static void overrideSuffixes(long digit, String suffix) {
        suffixes.put(digit, suffix);
    }

    public static String getFormattedString(long value) {
        String text = "";
        if (value < 1_000_000) {
            text = KMNumbers.formatDecimalString(value);
        } else if (value >= 1_000_000) {
            text = KMNumbers.formatNumbers(value);
        }
        return text;
    }

    /*  format number test case:
        -123L: -123
        -1234L: -1.234
        -12345L: -12,3rb
        -123456L: -123,5rb
        -1234567L: -1,2jt
        -12345678L: -12,3jt
        -123456789L: -123,5jt
        -1234567890L: -1,2M
        123L: 123
        1234L: 1.234
        12345L: 12,3rb
        123456L: 123,5rb
        1234567L: 1,2jt
        12345678L: 12,3jt
        123456789L: 123,5jt
        1234567890L: 1,2M
        */
    public static String formatNumbers(Long number) {
        boolean isNegative = false;
        if (number < 0) {
            isNegative = true;
            number = -1 * number;
        }
        if (number < 1000) {
            return (isNegative?"-":"" ) + numberFormat.format(number);
        }
        int exp = (int) (Math.log(number) / Math.log(1000));
        String result = formatString(number, exp);
        return (isNegative?"-":"" ) + result;
    }

    public static String formatNumbers(Float number) {
        boolean isNegative = false;
        if (number < 0) {
            isNegative = true;
            number = -1 * number;
        }
        if (number < 1000) {
            return (isNegative?"-":"" ) + numberFormat.format(number);
        }
        int exp = (int) (Math.log(number) / Math.log(1000));
        String result = formatString(number, exp);
        return (isNegative?"-":"" ) + result;
    }

    public static String formatRupiahString(Context context, long numberToFormat){
        return context.getString(R.string.gm_statistic_rupiah_format_text,
                KMNumbers.formatDecimalString(numberToFormat));
    }

    // convert (double) 1.12345 to "123,34%" (string)
    public static String formatToPercentString(Context context, double percent) {
        return context.getString(R.string.gm_statistic_percent_format_text, formatDouble2P(percent * 100));
    }

    /* test case for formatDecimalString
        -123L: -123
        -1234L: -1.234
        -12345L: -12.345
        -123456L: -123.456
        -1234567L: -1.234.567
        123L: 123
        1234L: 1.234
        12345L: 12.345
        123456L: 123.456
        1234567L: 1.234.567
     */
    public static String formatDecimalString (long numberToFormat){
        return formatDecimalString(numberToFormat, false);
    }

    public static String formatDecimalString (long numberToFormat, boolean useCommaAsThousand){
        if (useCommaAsThousand) {
            return NumberFormat.getNumberInstance(Locale.US).format(numberToFormat);
        } else {
            return NumberFormat.getNumberInstance(locale).format(numberToFormat);
        }
    }

    public static String formatDouble2P(Double number) {
        return String.format(locale, FORMAT_2_DOUBLE, number);
    }

    public static String formatDouble1P(Double number) {
        return String.format(locale, FORMAT_DOUBLE, number);
    }

    private static String formatString(Long number, Integer exp) {
        return String.format(locale, FORMAT, number / Math.pow(1000, exp), suffixes.get((long)Math.pow(1000, exp)));
    }

    private static String formatString(Float number, Integer exp) {
        return String.format(locale, FORMAT, number / Math.pow(1000, exp), suffixes.get((long)Math.pow(1000, exp)));
    }

}
