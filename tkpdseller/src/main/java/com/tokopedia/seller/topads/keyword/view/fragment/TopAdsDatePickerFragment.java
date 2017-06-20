package com.tokopedia.seller.topads.keyword.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.seller.lib.datepicker.DatePickerResultListener;
import com.tokopedia.seller.topads.view.fragment.BasePresenterFragment;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenter;

import java.util.Date;

/**
 * @author normansyahputa on 5/17/17.
 */
public abstract class TopAdsDatePickerFragment<T> extends BasePresenterFragment<T> implements
        DatePickerResultListener.DatePickerResult {

    private static final int REQUEST_CODE_DATE = 5;

    protected Date startDate;
    protected Date endDate;
    protected DatePickerResultListener datePickerResultListener;

    @Nullable
    protected TopAdsDatePickerPresenter datePickerPresenter;

    protected abstract TopAdsDatePickerPresenter getDatePickerPresenter();

    protected abstract void loadData();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialPresenter();
        if (datePickerPresenter == null) {
            throw new IllegalArgumentException("datePickerPresenter need to be initialized !!");
        }
        setHasOptionsMenu(true);
    }

    protected void initialPresenter() {
        datePickerPresenter = getDatePickerPresenter();
        datePickerResultListener = new DatePickerResultListener(this, REQUEST_CODE_DATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (datePickerPresenter.isDateUpdated(startDate, endDate)) {
            loadDateFromPresenter();
            loadData();
        }
    }

    private void loadDateFromPresenter() {
        startDate = datePickerPresenter.getStartDate();
        endDate = datePickerPresenter.getEndDate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (datePickerResultListener != null) {
            datePickerResultListener.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Override
    public void onDateChoosen(long sDate, long eDate, int lastSelection, int selectionType) {
        datePickerPresenter.saveDate(new Date(sDate), new Date(eDate));
        datePickerPresenter.saveSelectionDatePicker(selectionType, lastSelection);
        if (startDate == null || endDate == null) {
            return;
        }
        loadData();
    }

    protected void openDatePicker() {
        Intent intent = datePickerPresenter.getDatePickerIntent(getActivity(), startDate, endDate);
        startActivityForResult(intent, REQUEST_CODE_DATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        datePickerResultListener = null;
    }

}