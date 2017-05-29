package com.tokopedia.seller.topads.keyword.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.lib.datepicker.DatePickerResultListener;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenter;

import java.util.Date;

/**
 * @author normansyahputa on 5/17/17.
 *         another type of {@link com.tokopedia.seller.topads.view.fragment.TopAdsDatePickerFragment}
 */
public abstract class TopAdsDatePickerFragment<T> extends BaseDaggerFragment implements
        DatePickerResultListener.DatePickerResult {
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    private static final int REQUEST_CODE_DATE = 5;
    protected Date startDate;
    protected Date endDate;
    protected DatePickerResultListener datePickerResultListener;

    @Nullable
    protected TopAdsDatePickerPresenter datePickerPresenter;

    protected abstract TopAdsDatePickerPresenter getDatePickerPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialPresenter();
        if (datePickerPresenter == null) {
            throw new IllegalArgumentException("datePickerPresenter need to be initialized !!");
        }
    }

    protected void initialPresenter() {
        datePickerPresenter = getDatePickerPresenter();
        datePickerResultListener = new DatePickerResultListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (datePickerPresenter.isDateUpdated(startDate, endDate)) {
            loadDateFromPresenter();
            loadData();
        }
    }

    void loadDateFromPresenter() {
        startDate = datePickerPresenter.getStartDate();
        endDate = datePickerPresenter.getEndDate();
    }

    protected abstract void loadData();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (startDate != null && endDate != null) {
            outState.putLong(START_DATE, startDate.getTime());
            outState.putLong(END_DATE, endDate.getTime());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onRestoreState(savedInstanceState);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onRestoreState(Bundle savedState) {
        if (startDate == null && endDate == null) {
            startDate = new Date(savedState.getLong(START_DATE));
            endDate = new Date(savedState.getLong(END_DATE));
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
        loadDateFromPresenter();
        datePickerPresenter.saveDate(new Date(sDate), new Date(eDate));
        datePickerPresenter.saveSelectionDatePicker(selectionType, lastSelection);

        fetchData();
    }

    /**
     * fetch data after select date.
     */
    protected abstract void fetchData();

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
