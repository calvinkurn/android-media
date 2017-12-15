package com.tokopedia.seller.seller.info.view.util;

import com.tokopedia.seller.common.williamchart.util.GoldMerchantDateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by normansyahputa on 11/30/17.
 */

public final class SellerInfoDateUtil {

    private static final Locale locale = new Locale("in", "ID");
    private static final String KK_MM_Z = "kk:mm z";
    static final SimpleDateFormat sdf =
            new SimpleDateFormat(GoldMerchantDateUtils.YYYY_M_MDD, locale);
    static final SimpleDateFormat sdfHourEtc =
            new SimpleDateFormat(KK_MM_Z, locale);
    public static final int UNIX_MULTIPLIER = 1000;
    public static final int PREVIOUS_DAY_COUNT = 1;
    public static final int TODAY_DAY_COUNT1 = 0;

    static{
        sdfHourEtc.setTimeZone(TimeZone.getDefault());
    }

    private SellerInfoDateUtil(){

    }

    public static String fromUnixTime(long date, String[] monthNames){
        int conv = Integer.valueOf(sdf.format(new Date(date* UNIX_MULTIPLIER)));
        return GoldMerchantDateUtils.getDateWithoutYear(conv, monthNames);
    }

    public static String fromUnixTimeGetHourEtc(long date){
        return sdfHourEtc.format(new Date(date* UNIX_MULTIPLIER));
    }

    public static Date fromUnixTimeDate(long date){
        return new Date(date* UNIX_MULTIPLIER);
    }

    public static String yesterday(String[] monthNames){
        long unixTime = GoldMerchantDateUtils.getPreviousDate(Calendar.getInstance().getTimeInMillis(), PREVIOUS_DAY_COUNT) / UNIX_MULTIPLIER;
        return fromUnixTime(unixTime, monthNames);
    }

    public static String today(String[] monthNames){
        long unixTime = GoldMerchantDateUtils.getPreviousDate(Calendar.getInstance().getTimeInMillis(), TODAY_DAY_COUNT1) / UNIX_MULTIPLIER;
        return fromUnixTime(unixTime, monthNames);
    }

    public static boolean isToday(long date, String[] monthNames){
        return today(monthNames).equals(fromUnixTime(date,monthNames));
    }

    public static boolean isYesterday(long date, String[] monthNames){
        return yesterday(monthNames).equals(fromUnixTime(date, monthNames));
    }
}
