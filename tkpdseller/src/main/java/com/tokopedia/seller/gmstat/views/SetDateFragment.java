package com.tokopedia.seller.gmstat.views;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.views.adapter.SetDatePagerAdapter;

import static com.tokopedia.seller.gmstat.views.BaseGMStatActivity.IS_GOLD_MERCHANT;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.CUSTOM_END_DATE;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.CUSTOM_START_DATE;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.CUSTOM_TYPE;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.PERIOD_TYPE;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.SELECTION_PERIOD;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.SELECTION_TYPE;

/**
 * Created by normansyahputa on 11/25/16.
 */

public class SetDateFragment extends BasePresenterFragment {
    public static final String TAG = "SetDateFragment";
    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";
    private SetDate setDate;
    private TabLayout slidingTabs;
    private ViewPager setDateViewPager;
    private SetDatePagerAdapter setDatePagerAdapter;
    private boolean isGMStat;
    private int selectionPeriod;
    private long sDate;
    private long eDate;

    public static String reverseDate(String[] split) {
        String reverse = "";
        for (int i = split.length - 1; i >= 0; i--) {
            reverse += split[i];
        }
        return reverse;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity != null && activity instanceof SetDate) {
            setDate = (SetDate) activity;

            isGMStat = setDate.isGMStat();
            selectionPeriod = setDate.selectionPeriod();
            sDate = setDate.sDate();
            eDate = setDate.eDate();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            isGMStat = savedInstanceState.getBoolean(IS_GOLD_MERCHANT, false);
            selectionPeriod = savedInstanceState.getInt(SELECTION_PERIOD);
            sDate = savedInstanceState.getLong(CUSTOM_START_DATE);
            eDate = savedInstanceState.getLong(CUSTOM_END_DATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.set_date_layout, container, false);
        initViews(rootView);

        setupViewPager();
        return rootView;
    }

    protected void setupViewPager() {
        if(setDatePagerAdapter == null)
            setDatePagerAdapter = new SetDatePagerAdapter(getFragmentManager(),
                    getActivity(), isGMStat, selectionPeriod,
                    sDate, eDate);

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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_GOLD_MERCHANT, isGMStat);
        outState.putInt(SELECTION_PERIOD, selectionPeriod);
        outState.putLong(CUSTOM_START_DATE, sDate);
        outState.putLong(CUSTOM_END_DATE, eDate);
    }

    public void initViews(View rootView) {
        slidingTabs = (TabLayout) rootView.findViewById(R.id.sliding_tabs);
        setDateViewPager = (ViewPager) rootView.findViewById(R.id.set_date_viewpager);
    }

    //[START] unused methods
    @Override
    protected void setViewListener() {
    }

    @Override
    protected void initialVar() {
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    protected void onFirstTimeLaunched() {
    }

    @Override
    public void onSaveState(Bundle state) {
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
    }

    @Override
    public void onRestoreState(Bundle savedState) {
    }

    @Override
    protected void initialListener(Activity activity) {
    }

    @Override
    protected void setupArguments(Bundle arguments) {
    }

    @Override
    protected int getFragmentLayout() {
        return 0;
    }

    @Override
    protected void initView(View view) {
    }

    public interface SetDate {
        void returnStartAndEndDate(long startDate, long endDate, int lastSelection, int selectionType);

        boolean isGMStat();

        int selectionPeriod();

        int selectionType();

        long sDate();

        long eDate();
    }
    //[END] unused methods
}
