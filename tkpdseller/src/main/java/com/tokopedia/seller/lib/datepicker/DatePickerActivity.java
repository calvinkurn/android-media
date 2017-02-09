package com.tokopedia.seller.lib.datepicker;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.datepicker.constant.DatePickerConstant;
import com.tokopedia.seller.lib.datepicker.fragment.DatePickerCustomFragment;
import com.tokopedia.seller.lib.datepicker.fragment.DatePickerPeriodFragment;
import com.tokopedia.seller.lib.datepicker.model.PeriodRangeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 12/7/16.
 */

public class DatePickerActivity extends TActivity implements DatePickerPeriodFragment.Callback, DatePickerCustomFragment.Callback {

    public static final int OFFSCREEN_PAGE_LIMIT = 2;

    public static final int RESULT_CODE = 1;

    private ViewPager viewPager;

    private int selectionPeriod;
    private int selectionType;
    private long startDate;
    private long endDate;
    private long minStartDate;
    private long maxStartDate;
    private int maxDateRange;

    private ArrayList<PeriodRangeModel> periodRangeModelList;
    private DatePickerPeriodFragment datePickerPeriodFragment;
    private DatePickerCustomFragment mDatePickerCustomFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_date_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.pager);

        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
        }

        fetchIntent(getIntent().getExtras());
        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        DatePickerTabListener tabListener = new DatePickerTabListener(viewPager);
        tabLayout.setOnTabSelectedListener(tabListener);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_date_period));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_date_custom));
        viewPager.setCurrentItem(selectionType);
        String title = getIntent().getExtras().getString(DatePickerConstant.EXTRA_PAGE_TITLE);
        if (!TextUtils.isEmpty(title)) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().show();
        }
    }

    private PagerAdapter getViewPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>();
        datePickerPeriodFragment = DatePickerPeriodFragment.newInstance(selectionPeriod, periodRangeModelList);
        datePickerPeriodFragment.setCallback(this);
        fragmentList.add(datePickerPeriodFragment);
        mDatePickerCustomFragment = DatePickerCustomFragment.newInstance(startDate, endDate, minStartDate, maxStartDate, maxDateRange);
        mDatePickerCustomFragment.setCallback(this);
        fragmentList.add(mDatePickerCustomFragment);
        return new DatePickerTabPagerAdapter(getFragmentManager(), fragmentList);
    }

    @Override
    public void onDateSubmitted(long startDate, long endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        setResult();
    }

    @Override
    public void onDateSubmitted(int selectionPeriod, long startDate, long endDate) {
        this.selectionPeriod = selectionPeriod;
        this.startDate = startDate;
        this.endDate = endDate;
        setResult();
    }

    private void fetchIntent(Bundle extras) {
        if (extras != null) {
            startDate = extras.getLong(DatePickerConstant.EXTRA_CUSTOM_START_DATE, -1);
            endDate = extras.getLong(DatePickerConstant.EXTRA_CUSTOM_END_DATE, -1);
            selectionPeriod = extras.getInt(DatePickerConstant.EXTRA_SELECTION_PERIOD, 1);
            selectionType = extras.getInt(DatePickerConstant.EXTRA_SELECTION_TYPE, DatePickerConstant.SELECTION_TYPE_PERIOD_DATE);
            minStartDate = extras.getLong(DatePickerConstant.EXTRA_MIN_START_DATE, -1);
            maxStartDate = extras.getLong(DatePickerConstant.EXTRA_MAX_END_DATE, -1);
            maxDateRange = extras.getInt(DatePickerConstant.EXTRA_MAX_DATE_RANGE, -1);
            periodRangeModelList = extras.getParcelableArrayList(DatePickerConstant.EXTRA_DATE_PERIOD_LIST);
        }
    }

    public void setResult() {
        Intent intent = new Intent();
        intent.putExtra(DatePickerConstant.EXTRA_START_DATE, startDate);
        intent.putExtra(DatePickerConstant.EXTRA_END_DATE, endDate);
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, selectionPeriod);
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, viewPager.getCurrentItem());
        setResult(RESULT_CODE, intent);
        finish();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}