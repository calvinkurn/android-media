package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.lib.datepicker.PeriodRangeModel;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.presenter.TopAdsDatePickerPresenter;
import com.tokopedia.seller.topads.lib.datepicker.SetDateActivity;
import com.tokopedia.seller.topads.lib.datepicker.SetDateFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class TopAdsDatePickerFragment<T extends TopAdsDatePickerPresenter> extends BasePresenterFragment<T> {

    private static final int REQUEST_CODE_DATE = TopAdsDatePickerFragment.class.hashCode();

    protected Date startDate;
    protected Date endDate;

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected void initView(View view) {
    }

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
    public void onResume() {
        super.onResume();
        if (presenter.isDateUpdated(startDate, endDate)) {
            startDate = presenter.getStartDate();
            endDate = presenter.getEndDate();
            loadData();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_DATE && intent != null) {
            long startDateTime = intent.getLongExtra(SetDateFragment.START_DATE, -1);
            long endDateTime = intent.getLongExtra(SetDateFragment.END_DATE, -1);
            if (startDateTime > 0 && endDateTime > 0) {
                presenter.saveDate(new Date(startDateTime), new Date(endDateTime));
            }
        }
    }

    protected abstract void loadData();

    protected void openDatePicker() {
        Intent intent = new Intent(getActivity(), SetDateActivity.class);
        intent.putExtra(SetDateActivity.IS_GOLD_MERCHANT, true);
        Calendar todayCalendar = Calendar.getInstance();
        Calendar lastYearCalendar = Calendar.getInstance();
        lastYearCalendar.add(Calendar.YEAR, -1);

        intent.putExtra(SetDateActivity.CUSTOM_START_DATE, startDate.getTime());
        intent.putExtra(SetDateActivity.CUSTOM_END_DATE, endDate.getTime());

        todayCalendar.set(Calendar.HOUR_OF_DAY, 23);
        todayCalendar.set(Calendar.MINUTE, 59);
        todayCalendar.set(Calendar.SECOND, 59);

        lastYearCalendar.set(Calendar.HOUR_OF_DAY, 0);
        lastYearCalendar.set(Calendar.MINUTE, 0);
        lastYearCalendar.set(Calendar.SECOND, 0);

        intent.putExtra(SetDateActivity.MIN_START_DATE, lastYearCalendar.getTimeInMillis());
        intent.putExtra(SetDateActivity.MAX_END_DATE, todayCalendar.getTimeInMillis());
        intent.putExtra(SetDateActivity.MAX_DATE_RANGE, TopAdsConstant.MAX_DATE_RANGE);

        intent.putExtra(SetDateActivity.DATE_PERIOD_LIST, getPeriodRangeList(getActivity()));
        intent.putExtra(SetDateActivity.SELECTION_PERIOD, 2);
//        moveToSetDate.putExtra(SetDateActivity.SELECTION_TYPE, selectionType);

        startActivityForResult(intent, REQUEST_CODE_DATE);
    }

    public static ArrayList<PeriodRangeModel> getPeriodRangeList(Context context) {
        ArrayList<PeriodRangeModel> periodRangeList = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        periodRangeList.add(new PeriodRangeModel(endCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.label_today)));
        startCalendar.add(Calendar.DATE, -1);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), startCalendar.getTimeInMillis(), context.getString(R.string.yesterday)));
        startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DATE, -7);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.seven_days_ago)));
        startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DATE, -30);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.thirty_days_ago)));
        startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.DATE, 1);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.label_this_month)));
        return periodRangeList;
    }
}