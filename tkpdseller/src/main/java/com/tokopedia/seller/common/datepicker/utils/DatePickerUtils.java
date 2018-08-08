package com.tokopedia.seller.common.datepicker.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.tokopedia.seller.R;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nathaniel on 2/3/2017.
 */

public class DatePickerUtils {

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

    public static long convertStringToTimestamp(String dateFormat, String dateString) {
        try {
            return convertStringToDate(dateFormat, dateString).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Date convertStringToDate(String dateFormat, String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
        try {
            Date date = simpleDateFormat.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isDateEqual(String dateFormat, long startDate, long endDate, long comparedStartDate, long comparedEndDate) {
        return isDateEqual(dateFormat, startDate, comparedStartDate) && isDateEqual(dateFormat, endDate, comparedEndDate);
    }

    private static boolean isDateEqual(String dateFormat, long date, long comparedDate) {
        String dateString = convertTimestampToString(date, dateFormat);
        String comparedDateString = convertTimestampToString(comparedDate, dateFormat);
        return dateString.equalsIgnoreCase(comparedDateString);
    }

    private static String convertTimestampToString(long timestamp, String dateFormat) {
        Date date = new Date(timestamp);
        Format format = new SimpleDateFormat(dateFormat, Locale.US);
        return format.format(date);
    }

    public static long getDateDiff(long date1, long date2, TimeUnit timeUnit) {
        long diffInMillis = date2 - date1;
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public static DatePickerViewModel convertDatePickerFromIntent(Intent intent) {
        long starDate = intent.getLongExtra(DatePickerConstant.EXTRA_START_DATE, -1);
        long endDate = intent.getLongExtra(DatePickerConstant.EXTRA_END_DATE, -1);
        int lastSelection = intent.getIntExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, 1);
        int selectionType = intent.getIntExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, DatePickerConstant.SELECTION_TYPE_PERIOD_DATE);
        DatePickerViewModel datePickerViewModel = new DatePickerViewModel();
        datePickerViewModel.setStartDate(starDate);
        datePickerViewModel.setEndDate(endDate);
        datePickerViewModel.setDatePickerSelection(lastSelection);
        datePickerViewModel.setDatePickerType(selectionType);
        return datePickerViewModel;
    }
}