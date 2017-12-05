package com.tokopedia.seller.seller.info.view.util;

import com.tokopedia.seller.common.williamchart.util.GoldMerchantDateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by normansyahputa on 11/30/17.
 */

public class SellerInfoDateUtil {

    static final SimpleDateFormat sdf =
            new SimpleDateFormat(GoldMerchantDateUtils.YYYY_M_MDD, new Locale("in", "ID"));
    private String[] monthNames;

    public SellerInfoDateUtil(String[] monthNames){
        this.monthNames = monthNames;
    }

    public String fromUnixTimeNumberFormat(long date){
        int conv = Integer.valueOf(sdf.format(new Date(date*1000)));
        return GoldMerchantDateUtils.getDate(conv);
    }

    public String fromUnixTime(long date){
        int conv = Integer.valueOf(sdf.format(new Date(date*1000)));
        return GoldMerchantDateUtils.getDateWithoutYear(conv, monthNames);
    }

    public Date fromUnixTimeDate(long date){
        return new Date(date*1000);
    }

    public Date yesterdayDate(){
        return GoldMerchantDateUtils.getPreviousDateInDate(Calendar.getInstance().getTimeInMillis(), 7);
    }

    public Date todayDate(){
        return GoldMerchantDateUtils.getPreviousDateInDate(Calendar.getInstance().getTimeInMillis(), 0);
    }

    public String yesterdayNumberFormat(){
        long unixTime = GoldMerchantDateUtils.getPreviousDate(Calendar.getInstance().getTimeInMillis(), 7) / 1000;
        return fromUnixTimeNumberFormat(unixTime);
    }

    public String todayNumberFormat(){
        long unixTime = GoldMerchantDateUtils.getPreviousDate(Calendar.getInstance().getTimeInMillis(), 0) / 1000;
        return fromUnixTimeNumberFormat(unixTime);
    }

    public String yesterday(){
        long unixTime = GoldMerchantDateUtils.getPreviousDate(Calendar.getInstance().getTimeInMillis(), 7) / 1000;
        return fromUnixTime(unixTime);
    }

    public String today(){
        long unixTime = GoldMerchantDateUtils.getPreviousDate(Calendar.getInstance().getTimeInMillis(), 0) / 1000;
        return fromUnixTime(unixTime);
    }

    public boolean isToday(long date){
        return today().equals(fromUnixTime(date));
    }

    public boolean isYesterday(long date){
        return yesterday().equals(fromUnixTime(date));
    }
}
