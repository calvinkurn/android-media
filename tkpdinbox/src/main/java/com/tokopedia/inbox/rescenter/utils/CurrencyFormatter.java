package com.tokopedia.inbox.rescenter.utils;

import android.support.v4.util.Pair;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by sebastianuskh on 12/5/16.
 */

public class CurrencyFormatter {

    static private NumberFormat formatRupiah = new DecimalFormat("#,###,###");
    static private NumberFormat formatDollar = new DecimalFormat("#,###,###.00");

    public static String formatRupiah(String string) {
        if(string.isEmpty()) return "0";
        return formatRupiah.format(Double.valueOf(getRawString(string)));
    }

    public static String formatDotRupiah(String string) {
        if(string.isEmpty()) return "0";
        return formatRupiah.format(Double.valueOf(getRawString(string))).replaceAll(",",".");
    }

    public static Pair<String, Integer> typeFormatRupiah(String string, int posCursor){
        return new Pair<>(formatRupiah(string), posCursor);
    }

    public static String formatDollar(String string){
        if(string.isEmpty()) return "0";
        return formatDollar.format(Double.valueOf(getRawString(string)));
    }

    public static Pair<String, Integer> typeFormatDollar(String string, int posCursor){
        if(getCommaLocation(string) < posCursor){
            string = new StringBuilder(string).deleteCharAt(getCommaLocation(string) + 1).toString();
        } else {
            posCursor++;
        }
        return new Pair<>(formatDollar(string), posCursor);
    }

    public static Integer getCommaLocation(String string) {
        for(int i = 0; i < string.length(); i++){
            if (string.charAt(i) == '.'){
                return i;
            }
        }
        return string.length();
    }

    public static String getRawString(String string) {
        return string.replace(",", "");
    }
}
