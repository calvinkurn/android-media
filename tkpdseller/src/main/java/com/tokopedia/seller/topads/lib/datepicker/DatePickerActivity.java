package com.tokopedia.seller.topads.lib.datepicker;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.adapter.TopAdsDashboardPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 12/7/16.
 */

public class DatePickerActivity extends TActivity {

    public static final int OFFSCREEN_PAGE_LIMIT = 2;

    public static final String IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT";
    public static final String SELECTION_TYPE = "SELECTION_TYPE";
    public static final String SELECTION_PERIOD = "SELECTION_PERIOD";
    public static final String CUSTOM_START_DATE = "CUSTOM_START_DATE";
    public static final String CUSTOM_END_DATE = "CUSTOM_END_DATE";
    public static final String MIN_START_DATE = "MIN_START_DATE";
    public static final String MAX_END_DATE = "MAX_END_DATE";
    public static final String MAX_DATE_RANGE = "MAX_DATE_RANGE";
    public static final String DATE_PERIOD_LIST = "DATE_PERIOD_LIST";
    public static final String PAGE_TITLE = "PAGE_TITLE";
    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";

    public static final int RESULT_CODE = 1;
    public static final int SELECTION_TYPE_PERIOD_DATE = 0;
    public static final int SELECTION_TYPE_CUSTOM_DATE = 1;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private DatePickerTabListener tabListener;

    private int selectionPeriod;
    private int selectionType;
    private long startDate;
    private long endDate;
    private long minStartDate;
    private long maxStartDate;
    private int maxDateRange;
    private boolean isGoldMerchant;

    private ArrayList<PeriodRangeModel> periodRangeModelList;
    private PeriodFragment periodFragment;
    private CustomDateFragment customDateFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_date_picker);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.indicator);

        fetchIntent(getIntent().getExtras());

        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabListener = new DatePickerTabListener(viewPager);
        tabLayout.setOnTabSelectedListener(tabListener);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_date_period));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_date_custom));
        String title = getIntent().getExtras().getString(PAGE_TITLE);
        if (!TextUtils.isEmpty(title)) {
            getSupportActionBar().setTitle(title);
        }
    }

    private PagerAdapter getViewPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>();
        periodFragment = PeriodFragment.newInstance(selectionPeriod, periodRangeModelList);
        periodFragment.setCallback(new PeriodFragment.Callback() {
            @Override
            public void onDateSubmitted(int selectionPeriodResult, long startDateResult, long endDateResult) {
                selectionType = SELECTION_TYPE_PERIOD_DATE;
                selectionPeriod = selectionPeriodResult;
                startDate = startDateResult;
                endDate = endDateResult;
                setResult();
            }
        });
        fragmentList.add(periodFragment);
        customDateFragment = CustomDateFragment.newInstance(startDate, endDate, minStartDate, maxStartDate, maxDateRange);
        customDateFragment.setCallback(new CustomDateFragment.Callback() {
            @Override
            public void onDateSubmitted(long startDateResult, long endDateResult) {
                selectionType = SELECTION_TYPE_CUSTOM_DATE;
                startDate = startDateResult;
                endDate = endDateResult;
                setResult();
            }
        });
        fragmentList.add(customDateFragment);
        return new TopAdsDashboardPagerAdapter(getFragmentManager(), fragmentList);
    }

    private void fetchIntent(Bundle extras) {
        if (extras != null) {
            isGoldMerchant = extras.getBoolean(IS_GOLD_MERCHANT, false);
            selectionPeriod = extras.getInt(SELECTION_PERIOD, 1);
            selectionType = extras.getInt(SELECTION_TYPE, SELECTION_TYPE_PERIOD_DATE);
            startDate = extras.getLong(CUSTOM_START_DATE, -1);
            endDate = extras.getLong(CUSTOM_END_DATE, -1);
            minStartDate = extras.getLong(MIN_START_DATE, -1);
            maxStartDate = extras.getLong(MAX_END_DATE, -1);
            maxDateRange = extras.getInt(MAX_DATE_RANGE, -1);
            periodRangeModelList = extras.getParcelableArrayList(DATE_PERIOD_LIST);
        }
    }

    public void setResult() {
        Intent intent = new Intent();
        intent.putExtra(START_DATE, startDate);
        intent.putExtra(END_DATE, endDate);
        intent.putExtra(SELECTION_PERIOD, selectionPeriod);
        intent.putExtra(SELECTION_TYPE, selectionType);
        setResult(RESULT_CODE, intent);
        finish();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}