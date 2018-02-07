package com.tokopedia.topads.dashboard.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.seller.base.view.activity.BaseTabActivity;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenter;

import java.util.Date;

/**
 * Created by Nathaniel on 1/20/2017.
 */
@Deprecated
public abstract class TopAdsDatePickerActivity<T> extends BaseTabActivity {

    protected T presenter;

    protected Date startDate;
    protected Date endDate;
    protected BaseDatePickerPresenter datePickerPresenter;

    protected abstract BaseDatePickerPresenter getDatePickerPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialPresenter();
    }

    protected void initialPresenter() {
        datePickerPresenter = getDatePickerPresenter();
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
        if (requestCode == DatePickerConstant.REQUEST_CODE_DATE && intent != null) {
            long startDateTime = intent.getLongExtra(DatePickerConstant.EXTRA_START_DATE, -1);
            long endDateTime = intent.getLongExtra(DatePickerConstant.EXTRA_END_DATE, -1);
            int selectionDatePickerType = intent.getIntExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, 0);
            int selectionDatePeriodIndex = intent.getIntExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, 0);
            onDatePickerChoose(selectionDatePickerType, selectionDatePeriodIndex);
            if (startDateTime > 0 && endDateTime > 0) {
                datePickerPresenter.saveDate(new Date(startDateTime), new Date(endDateTime));
                datePickerPresenter.saveSelectionDatePicker(selectionDatePickerType, selectionDatePeriodIndex);
            }
        }
    }

    protected void onDatePickerChoose(int selectionDatePickerType, int selectionDatePeriodIndex) {

    }

    protected void openDatePicker() {
        Intent intent = datePickerPresenter.getDatePickerIntent(this, startDate, endDate);
        startActivityForResult(intent, DatePickerConstant.REQUEST_CODE_DATE);
    }
}
