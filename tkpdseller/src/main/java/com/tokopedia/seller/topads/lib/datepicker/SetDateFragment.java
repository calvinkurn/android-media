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
import com.tokopedia.seller.R2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.tokopedia.seller.topads.lib.datepicker.SetDateActivity.CUSTOM_TYPE;
import static com.tokopedia.seller.topads.lib.datepicker.SetDateActivity.PERIOD_TYPE;

/**
 * Created by normansyahputa on 11/25/16.
 */

public class SetDateFragment extends Fragment {
    private SetDate setDate;
    private static final Locale locale = new Locale("in", "ID");

    public interface SetDate {
        void returnStartAndEndDate(long startDate, long endDate, int lastSelection, int selectionType);

        boolean isGMStat();

        int selectionPeriod();

        int selectionType();

        long sDate();

        long eDate();

        long getMinStartDate();

        long getMaxStartDate();

        int getMaxDateRange();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null && context instanceof SetDate) {
            setDate = (SetDate) context;
        }
    }

    @BindView(R2.id.sliding_tabs)
    TabLayout slidingTabs;

    @BindView(R2.id.set_date_viewpager)
    ViewPager setDateViewPager;
    SetDatePagerAdapter setDatePagerAdapter;

    Unbinder bind;

    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_date_picker, container, false);
        bind = ButterKnife.bind(this, rootView);
        setDatePagerAdapter = new SetDatePagerAdapter(getActivity().getSupportFragmentManager(),
                getActivity(), setDate.isGMStat(), setDate.selectionPeriod(),
                setDate.sDate(), setDate.eDate(), setDate.getMinStartDate(), setDate.getMaxStartDate(), setDate.getMaxDateRange());
        setDateViewPager.setAdapter(setDatePagerAdapter);
        slidingTabs.setupWithViewPager(setDateViewPager);

        switch (setDate.selectionType()) {
            case PERIOD_TYPE:
                setDateViewPager.setCurrentItem(0);
                break;
            case CUSTOM_TYPE:
                setDateViewPager.setCurrentItem(1);
                break;
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    public static class SetDatePagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[]{"PERIODE", "KUSTOM"};
        private Context context;
        private boolean isGM;
        private int lastSelectionPeriod;
        private long sDate;
        private long eDate;
        private long minStartDate;
        private long maxEndDate;
        private int maxDateRange;

        public SetDatePagerAdapter(FragmentManager fm, Context context, boolean isGM,
                                   int lastSelectionPeriod, long sDate, long eDate,
                                   long minStartDate, long maxEndDate, int maxDateRange) {
            super(fm);
            this.context = context;
            this.isGM = isGM;
            this.lastSelectionPeriod = lastSelectionPeriod;
            this.sDate = sDate;
            this.eDate = eDate;
            this.minStartDate = minStartDate;
            this.maxEndDate = maxEndDate;
            this.maxDateRange = maxDateRange;
            Log.d("MNORMANSYAH", "sDate " + getDateFormat(sDate) + " eDate " + getDateFormat(eDate));
        }

        @Override
        public int getCount() {
            if (!isGM)
                return 1;
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PeriodFragment.newInstance(lastSelectionPeriod, maxEndDate);
                case 1:
                default:
                    return CustomFragment.newInstance(sDate, eDate, minStartDate, maxEndDate, maxDateRange);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
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
    }

    public static String getDateFormat(long timeInMillis){
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timeInMillis);
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);
        return dateFormat.format(instance.getTime());
    }

    public static String getDateWithYear(int date, String[] monthNames){
        List<String> dateRaw = getDateRaw(date);
        String year = dateRaw.get(2);
        String month = dateRaw.get(1);
        month = monthNames[Integer.parseInt(month)-1];

        String day = String.valueOf(Integer.valueOf(dateRaw.get(0)));

        return day + " "+ month+" "+year;
    }

    private static List<String> getDateRaw(int date){
        List<String> result = new ArrayList<>();
        String s = Integer.toString(date);
        String year = s.substring(0, 4);
        String month = s.substring(4, 6);
        String day = s.substring(6);
        result.add(day);result.add(month);result.add(year);
        return result;
    }
}
