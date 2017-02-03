package com.tokopedia.seller.topads.lib.datepicker;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MenuItem;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.adapter.TopAdsDashboardPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 12/7/16.
 */

public class DatePickerActivity extends TActivity implements SetDateFragment.SetDate {

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

    public static final int MOVE_TO_SET_DATE = 1;
    public static final int PERIOD_TYPE = 0;
    public static final int CUSTOM_TYPE = 1;


    private ViewPager viewPager;
    private TabLayout tabLayout;

    private boolean isGoldMerchant;
    private boolean isAfterRotate;

    private int selectionPeriod;
    private int selectionType;
    private long sDate = -1;
    private long eDate = -1;
    private long minStartDate;
    private long maxStartDate;
    private int maxDateRange;
    private ArrayList<PeriodRangeModel> periodRangeModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_date_picker);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.indicator);

        if (!isAfterRotate) {
            fetchIntent(getIntent().getExtras());
        }

        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_date_period));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_date_custom));
        String title = getIntent().getExtras().getString(PAGE_TITLE);
        if (!TextUtils.isEmpty(title)) {
            getSupportActionBar().setTitle(title);
        }
        isAfterRotate = savedInstanceState != null;
    }

    private PagerAdapter getViewPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(PeriodFragment.newInstance(selectionPeriod, periodRangeModelList));
        fragmentList.add(CustomDateFragment.newInstance(sDate, eDate, minStartDate, maxStartDate, maxDateRange));
        return new TopAdsDashboardPagerAdapter(getFragmentManager(), fragmentList);
    }

    private void fetchIntent(Bundle extras) {
        if (extras != null) {
            isGoldMerchant = extras.getBoolean(IS_GOLD_MERCHANT, false);
            selectionPeriod = extras.getInt(SELECTION_PERIOD, 1);
            selectionType = extras.getInt(SELECTION_TYPE, PERIOD_TYPE);
            sDate = extras.getLong(CUSTOM_START_DATE, -1);
            eDate = extras.getLong(CUSTOM_END_DATE, -1);
            minStartDate = extras.getLong(MIN_START_DATE, -1);
            maxStartDate = extras.getLong(MAX_END_DATE, -1);
            maxDateRange = extras.getInt(MAX_DATE_RANGE, -1);
            periodRangeModelList = extras.getParcelableArrayList(DATE_PERIOD_LIST);
        }
    }

    @Override
    public void returnStartAndEndDate(long startDate, long endDate, int lastSelection, int selectionType) {
        Intent intent = new Intent();
        intent.putExtra(START_DATE, startDate);
        intent.putExtra(END_DATE, endDate);
        if (lastSelection < 0) {
            lastSelection = selectionPeriod;
        }
        intent.putExtra(SELECTION_PERIOD, lastSelection);
        intent.putExtra(SELECTION_TYPE, selectionType);
        setResult(MOVE_TO_SET_DATE, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == com.tokopedia.core.R.id.home) {
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public long sDate() {
        return sDate;
    }

    @Override
    public long eDate() {
        return eDate;
    }

    @Override
    public String getScreenName() {
        return null;
    }
}