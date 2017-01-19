package com.tokopedia.seller.gmstat.views;


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

import java.util.Locale;

import static com.tokopedia.seller.gmstat.views.GMStatActivityFragment.getDateFormat;
import static com.tokopedia.seller.gmstat.views.SetDateActivity.CUSTOM_TYPE;
import static com.tokopedia.seller.gmstat.views.SetDateActivity.PERIOD_TYPE;

/**
 * Created by normansyahputa on 11/25/16.
 */

public class SetDateFragment extends Fragment {
    private SetDate setDate;
    private static final Locale locale = new Locale("in","ID");

    public interface SetDate{
        void returnStartAndEndDate(long startDate, long endDate, int lastSelection, int selectionType);
        boolean isGMStat();
        int selectionPeriod();
        int selectionType();
        long sDate();
        long eDate();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context != null && context instanceof SetDate){
            setDate = (SetDate) context;
        }
    }

    TabLayout slidingTabs;

    ViewPager setDateViewPager;
    SetDatePagerAdapter setDatePagerAdapter;

    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.set_date_layout, container, false);
        initView(rootView);
        setDatePagerAdapter = new SetDatePagerAdapter(getActivity().getSupportFragmentManager(),
                getActivity(), setDate.isGMStat(), setDate.selectionPeriod(),
                setDate.sDate(), setDate.eDate());
        setDateViewPager.setAdapter(setDatePagerAdapter);
        slidingTabs.setupWithViewPager(setDateViewPager);

        switch (setDate.selectionType()){
            case PERIOD_TYPE:
                setDateViewPager.setCurrentItem(0);
                break;
            case CUSTOM_TYPE:
                setDateViewPager.setCurrentItem(1);
                break;
        }
        return rootView;
    }

    public void initView(View rootView){
        slidingTabs = (TabLayout) rootView.findViewById(R.id.sliding_tabs);
        setDateViewPager = (ViewPager) rootView.findViewById(R.id.set_date_viewpager);
    }

    public static class SetDatePagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[] { "PERIODE", "KUSTOM" };
        private Context context;
        private boolean isGM;
        private int lastSelectionPeriod;
        private long sDate;
        private long eDate;

        public SetDatePagerAdapter(FragmentManager fm, Context context, boolean isGM,
                                   int lastSelectionPeriod, long sDate, long eDate) {
            super(fm);
            this.context = context;
            this.isGM = isGM;
            this.lastSelectionPeriod = lastSelectionPeriod;
            this.sDate = sDate;
            this.eDate = eDate;
            Log.d("MNORMANSYAH", "sDate "+getDateFormat(sDate)+" eDate "+getDateFormat(eDate));
        }

        @Override
        public int getCount() {
            if(!isGM)
                return 1;
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PeriodFragment.newInstance(lastSelectionPeriod);
                case 1:
                default:
                    return CustomFragment.newInstance(sDate,eDate);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }

    public static String reverseDate(String[] split) {
        String reverse = "";
        for(int i=split.length-1;i>=0;i--){
            reverse += split[i];
        }
        return reverse;
    }
}
