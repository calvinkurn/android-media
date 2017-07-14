package com.tokopedia.seller.common.datepicker.utils;

import android.content.Context;

import com.tokopedia.seller.R;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nathaniel on 2/3/2017.
 */

public class DatePickerUtils {

    private static final String RANGE_DATE_FORMAT = "dd MMM yyyy";
    private static final String RANGE_DATE_FORMAT_WITHOUT_YEAR = "dd MMM";

    public static String getReadableDate(Context context, long date) {
        String[] monthNamesAbrev = context.getResources().getStringArray(R.array.lib_date_picker_month_entries);
        DateFormat dateFormat = new SimpleDateFormat(DatePickerConstant.DATE_FORMAT, DatePickerConstant.LOCALE);
        String[] split = dateFormat.format(date).split(" ");
        return getDateWithYear(Integer.parseInt(DatePickerUtils.reverseDate(split)), monthNamesAbrev);
    }

    public static String getDateWithYear(int date, String[] monthNames) {
        List<String> dateRaw = getDateRaw(date);
        String year = dateRaw.get(2);
        String month = dateRaw.get(1);
        month = monthNames[Integer.parseInt(month) - 1];

        String day = String.valueOf(Integer.valueOf(dateRaw.get(0)));

        return day + " " + month + " " + year;
    }

    private static List<String> getDateRaw(int date) {
        List<String> result = new ArrayList<>();
        String s = Integer.toString(date);
        String year = s.substring(0, 4);
        String month = s.substring(4, 6);
        String day = s.substring(6);
        result.add(day);
        result.add(month);
        result.add(year);
        return result;
    }

    public static String reverseDate(String[] split) {
        String reverse = "";
        for (int i = split.length - 1; i >= 0; i--) {
            reverse += split[i];
        }
        return reverse;
    }

    public static String getRangeDateFormatted(Context context, Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return "";
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        String startDateFormatted = new SimpleDateFormat(RANGE_DATE_FORMAT, Locale.ENGLISH).format(startDate);
        String endDateFormatted = new SimpleDateFormat(RANGE_DATE_FORMAT, Locale.ENGLISH).format(endDate);
        if (startDateFormatted.equalsIgnoreCase(endDateFormatted)) {
            return endDateFormatted;
        }
        if (startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR)) {
            startDateFormatted = new SimpleDateFormat(RANGE_DATE_FORMAT_WITHOUT_YEAR, Locale.ENGLISH).format(startDate);
        }
        return context.getString(R.string.top_ads_range_date_text, startDateFormatted, endDateFormatted);
    }
}
