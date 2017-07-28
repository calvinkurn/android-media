package com.tokopedia.seller.base.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.ItemType;
import com.tokopedia.seller.base.view.listener.DatePickerList;
import com.tokopedia.seller.base.view.listener.DatePickerView;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenter;
import com.tokopedia.seller.common.datepicker.utils.DatePickerUtils;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.lib.widget.DateLabelView;

import javax.inject.Inject;

/**
 * Created by nathan on 7/21/17.
 */

public abstract class BaseListDateFragment<T extends ItemType> extends BaseListFragment<BlankPresenter, T>
        implements DatePickerList, DatePickerView {

    protected DateLabelView dateLabelView;

    @Inject
    public DatePickerPresenter datePickerPresenter;
    private DatePickerViewModel datePickerViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        datePickerPresenter.attachView(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateLabelView = (DateLabelView) view.findViewById(R.id.date_label_view);
        dateLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadDataForDate();
    }

    @Override
    protected final void loadData() {
        super.loadData();
    }

    @Override
    protected final void searchData() {
        super.searchData();
        reloadDataForDate();
    }

    @Override
    public void reloadDataForDate() {
        // important to reattach here, or the presenter will attach to the previous fragment when onResume
        datePickerPresenter.attachView(this);
        datePickerPresenter.fetchDatePickerSetting();
    }

    @Override
    public void onSuccessLoadDatePicker(DatePickerViewModel datePickerViewModel) {
        this.datePickerViewModel = datePickerViewModel;
        loadDataByDateAndPage(datePickerViewModel, page);
        setDateLabelView();
    }

    @Override
    public abstract void loadDataByDateAndPage(DatePickerViewModel datePickerViewModel, int page);

    private void setDateLabelView() {
        dateLabelView.setDate(datePickerViewModel.getStartDate(), datePickerViewModel.getEndDate());
    }

    @Override
    public void onErrorLoadDatePicker(Throwable throwable) {
        if (datePickerViewModel == null) {
            datePickerViewModel = getDefaultDateViewModel();
        } else {
            datePickerPresenter.saveDateSetting(datePickerViewModel);
        }
        setDateLabelView();
        loadDataByDateAndPage(datePickerViewModel, page);
    }

    @Override
    public void onSuccessSaveDatePicker() {
        // Do nothing
    }

    @Override
    public void onErrorSaveDatePicker(Throwable throwable) {
        // Do nothing
    }

    @Override
    public void onSuccessClearDatePicker() {
        // Do nothing
    }

    @Override
    public void onErrorClearDatePicker(Throwable throwable) {
        // Do nothing
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == DatePickerConstant.REQUEST_CODE_DATE && intent != null) {
            long startDate = intent.getLongExtra(DatePickerConstant.EXTRA_START_DATE, -1);
            long endDate = intent.getLongExtra(DatePickerConstant.EXTRA_END_DATE, -1);
            if (startDate > 0 && endDate > 0) {
                onDateSelected(intent);
            }
        }
    }

    protected void onDateSelected(Intent intent) {
        DatePickerViewModel datePickerViewModel = DatePickerUtils.convertDatePickerFromIntent(intent);
        if (datePickerViewModel!= null && !datePickerViewModel.equal(this.datePickerViewModel)) {
            datePickerPresenter.saveDateSetting(datePickerViewModel);
        }
    }

    @Override
    public void openDatePicker() {
        startActivityForResult(getDatePickerIntent(datePickerViewModel), DatePickerConstant.REQUEST_CODE_DATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        datePickerPresenter.detachView();
    }
}