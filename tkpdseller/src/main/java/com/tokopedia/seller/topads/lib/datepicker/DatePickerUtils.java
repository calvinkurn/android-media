package com.tokopedia.seller.topads.lib.datepicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 2/3/2017.
 */

public class DatePickerUtils {

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

    public interface PeriodListener {
        void updateCheck(boolean checked, int index);

        boolean isAllNone(boolean checked, int index);
    }

    public static class BasePeriodModel {
        public int type = -1;

        public BasePeriodModel(int type) {
            this.type = type;
        }

        public BasePeriodModel() {

        }
    }
}
