package com.tokopedia.seller.common.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by normansyahputa on 12/16/16.
 */
public class KMNumbers {

    public static final long DIGIT_THOUSAND = 1000L;
    public static final long DIGIT_MILLION = 1000000L;

    public static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    public static final String FORMAT_2_DOUBLE = "%.2f";
    public static final String SUFFIX_FORMAT = "%s%s";
    public static final String RUPIAH_FORMAT = "Rp %s";
    public static final String PERCENT_FORMAT = "%s%%";
    private static final Locale locale = new Locale("in", "ID");
    public static NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
    public static DecimalFormat amountDf = (DecimalFormat) NumberFormat.getNumberInstance(locale);

    public static final String SUFFIX_RB = "rb";
    public static final String SUFFIX_JT = "jt";
    public static final String SUFFIX_M = "M";
    public static final String SUFFIX_T = "T";
    public static final String SUFFIX_B = "B";

    static {
        suffixes.put(1000L, SUFFIX_RB);
        suffixes.put(1000000L, SUFFIX_JT);
        suffixes.put(1000000000L, SUFFIX_M);
        suffixes.put(1000000000000L, SUFFIX_T);
        suffixes.put(1000000000000000L, SUFFIX_B);
        amountDf.applyPattern(".0");
        amountDf.setRoundingMode(RoundingMode.DOWN);
    }

    public static void overrideSuffixes(long digit, String suffix) {
        suffixes.put(digit, suffix);
    }

    public static String getSummaryString(long value) {
        String text = "";
        if (Math.abs(value) < 10_000) {
            text = KMNumbers.formatDecimalString(value);
        } else {
            text = KMNumbers.formatSuffixNumbers(value);
        }
        return text;
    }

    /*  format number test case:
        -123L: -123
        -1234L: -1,2rb
        -12345L: -12,3rb
        -123456L: -123,5rb
        -1234567L: -1,2jt
        -12345678L: -12,3jt
        -123456789L: -123,5jt
        -1234567890L: -1,2M
        123L: 123
        1234L: 1,2rb
        12345L: 12,3rb
        123456L: 123,5rb
        1234567L: 1,2jt
        12345678L: 12,3jt
        123456789L: 123,5jt
        1234567890L: 1,2M
        */
    public static String formatSuffixNumbers(Long number) {
        boolean isNegative = false;
        if (number < 0) {
            isNegative = true;
            number = -1 * number;
        }
        if (number < 1000) {
            return (isNegative ? "-" : "") + numberFormat.format(number);
        }
        int exp = (int) (Math.log(number) / Math.log(1000));
        String result = formatString(number, exp);
        return (isNegative ? "-" : "") + result;
    }

    public static String formatSuffixNumbers(Float number) {
        boolean isNegative = false;
        if (number < 0) {
            isNegative = true;
            number = -1 * number;
        }
        if (number < 1000) {
            return (isNegative ? "-" : "") + numberFormat.format(number);
        }
        int exp = (int) (Math.log(number) / Math.log(1000));
        String result = formatString(number, exp);
        return (isNegative ? "-" : "") + result;
    }

    /*
        123L: Rp123
        1234L: Rp1.234
        12345L: Rp12.345
        123456L: Rp123.456
        1234567L: Rp1.234.567
     */
    public static String formatRupiahString(long numberToFormat) {
        return String.format(RUPIAH_FORMAT, KMNumbers.formatDecimalString(numberToFormat));
    }

    // convert (double) 1.12345 to "123,35%" (string) rounded
    // (double) 0.12 to 12% (without 00)
    // double 0.0 to 0%
    // double 0.001 to 0.00%
    public static String formatToPercentString(double percent) {
        // check if integer
        double percentTimes100 = percent * 100;
        String percentString;
        // if the difference between rounded and not rounded is 0
        if (percentTimes100 - Math.floor(percentTimes100) == 0) {
            percentString = String.valueOf(Math.round(percentTimes100));
        } else {
            percentString = formatDouble2P(percentTimes100);
        }
        return String.format(PERCENT_FORMAT, percentString);
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
    public static String formatDecimalString(long numberToFormat) {
        return formatDecimalString(numberToFormat, false);
    }

    public static String formatDecimalString(long numberToFormat, boolean useCommaAsThousand) {
        if (useCommaAsThousand) {
            return NumberFormat.getNumberInstance(Locale.US).format(numberToFormat);
        } else {
            return NumberFormat.getNumberInstance(locale).format(numberToFormat);
        }
    }

    /**
     * 25.123 => 25.12
     * 25.2555 => 25.26
     * 26 => 26
     */
    public static String formatDouble2PCheckRound(double number, boolean useCommaAsThousand) {
        // check if integer
        String numberString;
        // if the difference between rounded and not rounded is 0
        if (number - Math.floor(number) == 0) {
            numberString = formatDecimalString(Math.round(number), useCommaAsThousand);
        } else {
            numberString = formatDouble2P(number, useCommaAsThousand);
        }
        return numberString;
    }

    /**
     * 25.123 => 25.12
     * 25.2555 => 25.26
     */
    public static String formatDouble2P(Double number, boolean useCommaAsThousand) {
        if (useCommaAsThousand) {
            return String.format(Locale.US, FORMAT_2_DOUBLE, number);
        } else {
            return String.format(locale, FORMAT_2_DOUBLE, number);
        }
    }

    public static String formatDouble2P(Double number) {
        return String.format(locale, FORMAT_2_DOUBLE, number);
    }

    // 12345 to 12,3rb
    // 12999 to 12,9rb
    // 12001 to 12rb
    // 12000 to 12rb
    private static String formatString(Number number, Integer exp) {
        double mainNumber = number.doubleValue() / Math.pow(1000, exp);
        String numberString;
        if ( Math.floor( mainNumber*10) % 10 == 0 ) { // the last decimal is 0?
            numberString = String.valueOf(Math.round(mainNumber));
        } else {
            numberString = amountDf.format(mainNumber);
        }
        return String.format(locale, SUFFIX_FORMAT,
                numberString,
                suffixes.get((long) Math.pow(1000, exp)));
    }

}
