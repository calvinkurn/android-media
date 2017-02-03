package com.tokopedia.seller.topads.lib.datepicker;


/**
 * Created by normansyahputa on 11/25/16.
 */

public class SetDateFragment {

    public static String reverseDate(String[] split) {
        String reverse = "";
        for (int i = split.length - 1; i >= 0; i--) {
            reverse += split[i];
        }
        return reverse;
    }

    public interface SetDate {
        void returnStartAndEndDate(long startDate, long endDate, int lastSelection, int selectionType);

        long sDate();

        long eDate();
    }

    public interface PeriodListener {
        void updateCheck(boolean checked, int index);

        boolean isAllNone(boolean checked, int index);
    }

    public static class BasePeriodModel {
        int type = -1;

        public BasePeriodModel(int type) {
            this.type = type;
        }

        public BasePeriodModel() {

        }
    }
}