package com.tokopedia.topads.keyword.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.seller.base.view.adapter.ItemType;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.listener.DatePickerResultListener;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenter;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenterImpl;

import java.util.Date;

/**
 * @author normansyahputa on 5/17/17.
 *         another type of {@link com.tokopedia.topads.dashboard.view.fragment.TopAdsAdListFragment}
 */

public abstract class TopAdsBaseListFragment<T, U extends ItemType> extends BaseListFragment<T, U> implements
        DatePickerResultListener.DatePickerResult {

    protected Date startDate;
    protected Date endDate;
    protected DatePickerResultListener datePickerResultListener;

    @Nullable
    protected BaseDatePickerPresenter datePickerPresenter;

    protected BaseDatePickerPresenter getDatePickerPresenter() {
        BaseDatePickerPresenterImpl baseDatePickerPresenter = new BaseDatePickerPresenterImpl(getActivity());
        return baseDatePickerPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    protected void initialPresenter() {
        super.initialPresenter();
        datePickerPresenter = getDatePickerPresenter();
        datePickerResultListener = new DatePickerResultListener(this, DatePickerConstant.REQUEST_CODE_DATE);
    }

    @Override
    protected void setActionVar() {
        // Avoid load date at first time
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
        if (datePickerPresenter == null) {
            return;
        }
        datePickerPresenter.saveDate(new Date(sDate), new Date(eDate));
        datePickerPresenter.saveSelectionDatePicker(selectionType, lastSelection);
        if (startDate == null || endDate == null) {
            return;
        }
        loadData();
    }

    protected void openDatePicker() {
        if (datePickerPresenter == null) {
            return;
        }
        Intent intent = datePickerPresenter.getDatePickerIntent(getActivity(), startDate, endDate);
        startActivityForResult(intent, DatePickerConstant.REQUEST_CODE_DATE);
    }

    @Override
    protected void setAndSearchForPage(int page) {
        if (startDate == null || endDate == null) {
            return;
        }
        this.currentPage = page;
        searchForPage(page);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        datePickerResultListener = null;
    }
}