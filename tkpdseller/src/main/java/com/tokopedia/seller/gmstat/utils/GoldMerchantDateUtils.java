package com.tokopedia.seller.gmstat.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by normansyahputa on 1/19/17.
 */
public class GoldMerchantDateUtils {
    private static final Locale locale = new Locale("in","ID");
    private static final String TAG = "GoldMerchantDateUtils";

    public static String getDateWithYear(int date, String[] monthNames){
        List<String> dateRaw = getDateRaw(date);
        String year = dateRaw.get(2);
        String month = dateRaw.get(1);
        month = monthNames[Integer.parseInt(month)-1];

        String day = String.valueOf(Integer.valueOf(dateRaw.get(0)));
        Log.d(TAG, "bulan "+month+" tanggal "+day+" rawDate "+date);

        return day + " "+ month+" "+year;
    }

    public static long getDateWithYear(int date){
        List<String> dateRaw = getDateRaw(date);
        String year = dateRaw.get(2);
        String month = dateRaw.get(1);
        String day = String.valueOf(Integer.valueOf(dateRaw.get(0)));
        Calendar instance = Calendar.getInstance();
        instance.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));

        return instance.getTimeInMillis();
    }

    public static String getDateWithYear(String date, String[] monthNames){
        List<String> dateRaw = getDateRaw(date);
        String year = dateRaw.get(2);
        String month = dateRaw.get(1);
        month = monthNames[Integer.parseInt(month)-1];

        String day = String.valueOf(Integer.valueOf(dateRaw.get(0)));
        Log.d(TAG, "bulan "+month+" tanggal "+day+" rawDate "+date);

        return day + " "+ month+" "+year;
    }

    public static String getDate(Integer date){
        List<String> dateRaw = getDateRaw(date);
        String month = dateRaw.get(1);
        String day = dateRaw.get(0);
        Log.d(TAG, "bulan "+month+" tanggal "+day);

        return day + " "+ month;
    }

    public static String getDateRaw(String label , String[] monthNames){
        String[] split = label.split(" ");
        return split[0]+" "+monthNames[Integer.parseInt(split[1])-1];
    }

    private static List<String> getDateRaw(String s){
        List<String> result = new ArrayList<>();
        String year = s.substring(0, 4);
        String month = s.substring(4, 6);
        String day = s.substring(6);
        Log.d(TAG, "getDateRaw : "+s+ " day "+day+" int "+s);
        result.add(day);result.add(month);result.add(year);
        return result;
    }

    private static List<String> getDateRaw(int date){
        List<String> result = new ArrayList<>();
        String s = Integer.toString(date);
        String year = s.substring(0, 4);
        String month = s.substring(4, 6);
        String day = s.substring(6);
        Log.d(TAG, "getDateRaw : "+s+ " day "+day+" int "+date);
        result.add(day);result.add(month);result.add(year);
        return result;
    }

    public static String getDateFormat(long timeInMillis){
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timeInMillis);
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);
        return dateFormat.format(instance.getTime());
    }
}
