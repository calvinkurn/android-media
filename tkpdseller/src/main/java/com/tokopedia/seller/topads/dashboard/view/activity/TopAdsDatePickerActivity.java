package com.tokopedia.seller.topads.dashboard.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.base.view.constant.ConstantView;
import com.tokopedia.seller.lib.datepicker.constant.DatePickerConstant;
import com.tokopedia.seller.base.view.presenter.BaseDatePickerPresenter;

import java.util.Date;

/**
 * Created by Nathaniel on 1/20/2017.
 */

public abstract class TopAdsDatePickerActivity<T> extends BasePresenterActivity<T> {

    protected Date startDate;
    protected Date endDate;
    protected BaseDatePickerPresenter datePickerPresenter;

    protected abstract BaseDatePickerPresenter getDatePickerPresenter();

    @Override
    protected void setupURIPass(Uri uri) {

    }

    @Override
    protected void setupBundlePass(Bundle bundle) {

    }

    @Override
    protected void initialPresenter() {
        datePickerPresenter = getDatePickerPresenter();
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

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
        if (requestCode == ConstantView.REQUEST_CODE_DATE && intent != null) {
            long startDateTime = intent.getLongExtra(DatePickerConstant.EXTRA_START_DATE, -1);
            long endDateTime = intent.getLongExtra(DatePickerConstant.EXTRA_END_DATE, -1);
            int selectionDatePickerType = intent.getIntExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, 0);
            int selectionDatePeriodIndex = intent.getIntExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, 0);
            if (startDateTime > 0 && endDateTime > 0) {
                datePickerPresenter.saveDate(new Date(startDateTime), new Date(endDateTime));
                datePickerPresenter.saveSelectionDatePicker(selectionDatePickerType, selectionDatePeriodIndex);
            }
        }
    }

    protected void openDatePicker() {
        Intent intent = datePickerPresenter.getDatePickerIntent(this, startDate, endDate);
        startActivityForResult(intent, ConstantView.REQUEST_CODE_DATE);
    }
}
