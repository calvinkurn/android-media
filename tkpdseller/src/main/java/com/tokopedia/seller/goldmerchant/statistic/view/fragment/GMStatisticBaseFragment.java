package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.base.view.adapter.ItemType;
import com.tokopedia.seller.base.view.constant.ConstantView;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.base.view.presenter.BaseDatePickerPresenter;
import com.tokopedia.seller.base.view.presenter.BaseDatePickerPresenterImpl;
import com.tokopedia.seller.lib.datepicker.DatePickerResultListener;

import java.util.Date;

/**
 * @author normansyahputa on 5/17/17.
 *         another type of {@link com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsAdListFragment}
 */

public abstract class GMStatisticBaseFragment extends BaseDaggerFragment implements
        DatePickerResultListener.DatePickerResult {

    protected Date startDate;
    protected Date endDate;
    protected DatePickerResultListener datePickerResultListener;

    @Nullable
    protected BaseDatePickerPresenter datePickerPresenter;

    protected BaseDatePickerPresenter getDatePickerPresenter() {
        return new BaseDatePickerPresenterImpl(getActivity());
    }

    protected abstract void loadData();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        startActivityForResult(intent, ConstantView.REQUEST_CODE_DATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        datePickerResultListener = null;
    }
}