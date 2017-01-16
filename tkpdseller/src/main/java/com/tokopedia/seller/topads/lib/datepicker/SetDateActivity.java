package com.tokopedia.seller.topads.lib.datepicker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.tokopedia.seller.R;

import butterknife.ButterKnife;

/**
 * Created by normansyahputa on 12/7/16.
 */

public class SetDateActivity extends AppCompatActivity implements SetDateFragment.SetDate {

    public static final String IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT";
    public static final int MOVE_TO_SET_DATE = 1;

    public static final String SELECTION_TYPE = "SELECTION_TYPE";
    public static final String SELECTION_PERIOD = "SELECTION_PERIOD";
    public static final String CUSTOM_START_DATE = "CUSTOM_START_DATE";
    public static final String CUSTOM_END_DATE = "CUSTOM_END_DATE";
    public static final String MIN_START_DATE = "MIN_START_DATE";
    public static final String MAX_END_DATE = "MAX_END_DATE";
    public static final String MAX_DATE_RANGE = "MAX_DATE_RANGE";

    public static final int PERIOD_TYPE = 0;
    public static final int CUSTOM_TYPE = 1;

    private boolean isGoldMerchant;
    private boolean isAfterRotate;

    int green600;

    int tkpdMainGreenColor;
    private int selectionPeriod;
    private int selectionType;
    private long sDate = -1, eDate = -1;
    private long minStartDate;
    private long maxStartDate;
    private int maxDateRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isAfterRotate) {
            fetchIntent(getIntent().getExtras());
        }
        setContentView(R.layout.activity_date_picker);
        ButterKnife.bind(this);
        green600 = ContextCompat.getColor(this, R.color.green_600);
        tkpdMainGreenColor = ContextCompat.getColor(this, R.color.tkpd_main_green);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(green600);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setBackgroundColor(tkpdMainGreenColor);
        }
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
        }
        isAfterRotate = savedInstanceState != null;
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
        }
    }

    @Override
    public void returnStartAndEndDate(long startDate, long endDate, int lastSelection, int selectionType) {
        Intent intent = new Intent();
        intent.putExtra(SetDateFragment.START_DATE, startDate);
        intent.putExtra(SetDateFragment.END_DATE, endDate);
        if (lastSelection < 0) {
            lastSelection = selectionPeriod;
        }
        intent.putExtra(SELECTION_PERIOD, lastSelection);
        intent.putExtra(SELECTION_TYPE, selectionType);
        setResult(MOVE_TO_SET_DATE, intent);
        finish();
    }

    @Override
    public boolean isGMStat() {
        return isGoldMerchant;
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
    public int selectionPeriod() {
        return selectionPeriod;
    }

    @Override
    public int selectionType() {
        return selectionType;
    }

    @Override
    public long sDate() {
        return sDate;
    }

    @Override
    public long eDate() {
        return eDate;
    }

    public long getMinStartDate() {
        return minStartDate;
    }

    public long getMaxStartDate() {
        return maxStartDate;
    }

    public int getMaxDateRange() {
        return maxDateRange;
    }
}