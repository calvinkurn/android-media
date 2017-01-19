package com.tokopedia.seller.gmstat.views;


import android.app.Activity;
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

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.views.adapter.SetDatePagerAdapter;

import java.util.Locale;

import static com.tokopedia.seller.gmstat.views.GMStatActivityFragment.getDateFormat;
import static com.tokopedia.seller.gmstat.views.SetDateActivity.CUSTOM_TYPE;
import static com.tokopedia.seller.gmstat.views.SetDateActivity.PERIOD_TYPE;

/**
 * Created by normansyahputa on 11/25/16.
 */

public class SetDateFragment extends BasePresenterFragment {
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity != null && activity instanceof SetDate){
            setDate = (SetDate) activity;
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
        initViews(rootView);
        setDatePagerAdapter = new SetDatePagerAdapter(getActivity().getFragmentManager(),
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

    public void initViews(View rootView){
        slidingTabs = (TabLayout) rootView.findViewById(R.id.sliding_tabs);
        setDateViewPager = (ViewPager) rootView.findViewById(R.id.set_date_viewpager);
    }

    public static String reverseDate(String[] split) {
        String reverse = "";
        for(int i=split.length-1;i>=0;i--){
            reverse += split[i];
        }
        return reverse;
    }

    //[START] unused methods
    @Override protected void setViewListener() {}

    @Override protected void initialVar() {}

    @Override protected void setActionVar() {}

    @Override protected boolean isRetainInstance() { return false; }

    @Override protected void onFirstTimeLaunched() { }

    @Override public void onSaveState(Bundle state) { }

    @Override protected boolean getOptionsMenuEnable() { return false; }

    @Override protected void initialPresenter() {}

    @Override public void onRestoreState(Bundle savedState) {}

    @Override protected void initialListener(Activity activity) {}

    @Override protected void setupArguments(Bundle arguments) {}

    @Override protected int getFragmentLayout() { return 0; }

    @Override protected void initView(View view) {}
    //[END] unused methods
}
