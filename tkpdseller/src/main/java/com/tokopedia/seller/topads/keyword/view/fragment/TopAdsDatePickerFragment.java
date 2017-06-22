package com.tokopedia.seller.topads.keyword.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.seller.base.view.constant.ConstantView;
import com.tokopedia.seller.lib.datepicker.DatePickerResultListener;
import com.tokopedia.seller.topads.view.fragment.BasePresenterFragment;
import com.tokopedia.seller.base.view.presenter.BaseDatePickerPresenter;

import java.util.Date;

/**
 * @author normansyahputa on 5/17/17.
 */
public abstract class TopAdsDatePickerFragment<T> extends BasePresenterFragment<T> implements
        DatePickerResultListener.DatePickerResult {

    protected Date startDate;
    protected Date endDate;
    protected DatePickerResultListener datePickerResultListener;

    @Nullable
    protected BaseDatePickerPresenter datePickerPresenter;

    protected abstract BaseDatePickerPresenter getDatePickerPresenter();

    protected abstract void loadData();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialPresenter();
        setHasOptionsMenu(true);
    }

    protected void initialPresenter() {
        datePickerPresenter = getDatePickerPresenter();
        datePickerResultListener = new DatePickerResultListener(this, ConstantView.REQUEST_CODE_DATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (datePickerPresenter!= null && datePickerPresenter.isDateUpdated(startDate, endDate)) {
            startDate = datePickerPresenter.getStartDate();
            endDate = datePickerPresenter.getEndDate();
            loadData();
        }
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
        startActivityForResult(intent, ConstantView.REQUEST_CODE_DATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        datePickerResultListener = null;
    }

}