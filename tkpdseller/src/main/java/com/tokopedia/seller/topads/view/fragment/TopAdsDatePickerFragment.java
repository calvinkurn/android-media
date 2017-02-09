package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.topads.presenter.TopAdsDatePickerPresenter;
import com.tokopedia.seller.lib.datepicker.DatePickerActivity;

import java.util.Date;

public abstract class TopAdsDatePickerFragment<T> extends BasePresenterFragment<T> {

    private static final int REQUEST_CODE_DATE = 5;
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";

    protected Date startDate;
    protected Date endDate;
    protected TopAdsDatePickerPresenter datePickerPresenter;

    protected abstract TopAdsDatePickerPresenter getDatePickerPresenter();

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {
        if(startDate != null && endDate != null) {
            state.putLong(START_DATE, startDate.getTime());
            state.putLong(END_DATE, endDate.getTime());
        }
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        if(startDate == null && endDate == null) {
            startDate = new Date(savedState.getLong(START_DATE));
            endDate = new Date(savedState.getLong(END_DATE));
        }
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        datePickerPresenter = getDatePickerPresenter();
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
        datePickerPresenter = getDatePickerPresenter();
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (datePickerPresenter.isDateUpdated(startDate, endDate)) {
            startDate = datePickerPresenter.getStartDate();
            endDate = datePickerPresenter.getEndDate();
            loadData();
        }
    }

    protected abstract void loadData();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_DATE && intent != null) {
            long startDateTime = intent.getLongExtra(DatePickerActivity.START_DATE, -1);
            long endDateTime = intent.getLongExtra(DatePickerActivity.END_DATE, -1);
            int selectionDatePickerType = intent.getIntExtra(DatePickerActivity.SELECTION_TYPE, 0);
            int selectionDatePeriodIndex = intent.getIntExtra(DatePickerActivity.SELECTION_PERIOD, 0);
            if (startDateTime > 0 && endDateTime > 0) {
                datePickerPresenter.saveDate(new Date(startDateTime), new Date(endDateTime));
                datePickerPresenter.saveSelectionDatePicker(selectionDatePickerType, selectionDatePeriodIndex);
            }
        }
    }

    protected void openDatePicker() {
        Intent intent = datePickerPresenter.getDatePickerIntent(getActivity(), startDate, endDate);
        startActivityForResult(intent, REQUEST_CODE_DATE);
    }
}