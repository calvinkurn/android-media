package com.tokopedia.seller.topads.lib.datepicker;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.tokopedia.seller.topads.lib.datepicker.DatePickerActivity.CUSTOM_TYPE;
import static com.tokopedia.seller.topads.lib.datepicker.DatePickerActivity.PERIOD_TYPE;

/**
 * Created by normansyahputa on 11/25/16.
 */

public class SetDateFragment {

    public interface SetDate {
        void returnStartAndEndDate(long startDate, long endDate, int lastSelection, int selectionType);

        long sDate();

        long eDate();
    }

    public interface PeriodListener {
        void updateCheck(boolean checked, int index);

        boolean isAllNone(boolean checked, int index);
    }

    public static String reverseDate(String[] split) {
        String reverse = "";
        for (int i = split.length - 1; i >= 0; i--) {
            reverse += split[i];
        }
        return reverse;
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