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
            int selectionDatePickerType = intent.getIntExtra(SetDateActivity.SELECTION_TYPE, 0);
            int selectionDatePeriodIndex = intent.getIntExtra(SetDateActivity.SELECTION_PERIOD, 0);
            if (startDateTime > 0 && endDateTime > 0) {
                presenter.saveDate(new Date(startDateTime), new Date(endDateTime));
                presenter.saveSelectionDatePicker(selectionDatePickerType, selectionDatePeriodIndex);
            }
        }
    }

    protected abstract void loadData();

    protected void openDatePicker() {
        Intent intent = presenter.getDatePickerIntent(getActivity(), startDate, endDate);
        startActivityForResult(intent, REQUEST_CODE_DATE);
    }
}