package com.tokopedia.seller.topads.view.library.util;

import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by normansyahputa on 12/16/16.
 */
public final class KMNumbers {

    public static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    public static final String FORMAT_DOUBLE = "%.1f";
    public static final String FORMAT = "%.1f%s";
    public static final String SUFFIXES = "KMGTPE";
    public static final String COMMA = ",";
    public static final String DOT = ".";
    private static final Locale locale = new Locale("in","ID");

    static {
        suffixes.put(1000L, "Rb");
        suffixes.put(1000000L, "Jt");
        suffixes.put(1000000000L, "M");
        suffixes.put(1000000000000L, "T");
    }

    public static void overrideSuffixes(long digit, String suffix){
        suffixes.put(digit, suffix);
    }

    public static String formatNumbers(Long number) {
        if (number >= 100000 || number<0){
            return formatNumbersBiggerThanHundredThousand(number);
        }

        if (number < 10000) return number.toString();

        int exp = (int) (Math.log(number) / Math.log(1000));
        String result = formatString(number, exp);
        //[START] dont delete this
//        String comma = COMMA;
//        String dot = DOT;
//        if(result.contains(comma)){
//            result = result.replaceAll(comma,dot);
//        }
        //[END] dont delete this
        return result;
    }

    public static String formatNumbers(Float number) {
        if (number >= 100000 || number<0){
            return formatNumbersBiggerThanHundredThousand(number);
        }

        if (number < 10000) return number.toString();

        int exp = (int) (Math.log(number) / Math.log(1000));
        String result = formatString(number, exp);
        //[START] dont delete this
//        String comma = COMMA;
//        String dot = DOT;
//        if(result.contains(comma)){
//            result = result.replaceAll(comma,dot);
//        }
        //[END] dont delete this
        return result;
    }

    private static String formatNumbersBiggerThanHundredThousand(Long number) {
        if (number == Long.MIN_VALUE) return formatNumbersBiggerThanHundredThousand(Long.MIN_VALUE + 1);
        if (number < 0) return "-" + formatNumbersBiggerThanHundredThousand(-number);
        if (number < 10000) return Long.toString(number);

        Map.Entry<Long, String> e = suffixes.floorEntry(number);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = number / (divideBy / 10);
        double v = truncated / 10d;
        return formatString(v) + suffix;
    }

    private static String formatNumbersBiggerThanHundredThousand(Float number) {
        if (number == Long.MIN_VALUE) return formatNumbersBiggerThanHundredThousand(Long.MIN_VALUE + 1);
        if (number < 0) return "-" + formatNumbersBiggerThanHundredThousand(-number);
        if (number < 10000) return Float.toString(number);

        Map.Entry<Long, String> e = suffixes.floorEntry((long) Math.round(number));
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        float truncated = number / (divideBy / 10);
        double v = truncated / 10d;
        return formatString(v) + suffix;
    }

    public static String formatString(Double number){
        return String.format(locale, FORMAT_DOUBLE, number);
    }

    private static String formatString(Long number, Integer exp) {
        return String.format(locale, FORMAT, number / Math.pow(1000, exp), suffixes.floorEntry(number).getValue());
    }

    private static String formatString(Float number, Integer exp) {
        return String.format(locale, FORMAT, number / Math.pow(1000, exp), suffixes.floorEntry((long) Math.round(number)).getValue());
    }
}
