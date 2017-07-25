package com.tokopedia.seller.base.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.listener.DatePicker;
import com.tokopedia.seller.base.view.listener.DatePickerView;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenter;
import com.tokopedia.seller.common.datepicker.utils.DatePickerUtils;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.lib.widget.DateLabelView;

import java.util.Date;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/17/17.
 *         another type of {@link com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsAdListFragment}
 */

public abstract class BaseDatePickerFragment extends BaseDaggerFragment implements DatePicker, DatePickerView {

    protected DateLabelView dateLabelView;

    @Inject
    public DatePickerPresenter datePickerPresenter;
    protected DatePickerViewModel datePickerViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dateLabelView = (DateLabelView) view.findViewById(R.id.date_label_view);
        dateLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        datePickerPresenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        // fix when after setOnResult, view is not attached
        if (!datePickerPresenter.isViewAttached()) {
            datePickerPresenter.attachView(this);
        }
        datePickerPresenter.fetchDatePickerSetting();
    }

    @Override
    public void onSuccessLoadDatePicker(DatePickerViewModel datePickerViewModel) {
        if (this.datePickerViewModel == null) {
            this.datePickerViewModel = datePickerViewModel;
            loadData();
        } else if (!DatePickerUtils.isDateEqual(DatePickerConstant.DATE_FORMAT,
                this.datePickerViewModel.getStartDate(), this.datePickerViewModel.getEndDate(),
                datePickerViewModel.getStartDate(), datePickerViewModel.getEndDate())) {
            datePickerPresenter.saveDateSetting(this.datePickerViewModel);
            loadData();
        }
    }

    @Override
    public void loadData() {
        dateLabelView.setDate(new Date(datePickerViewModel.getStartDate()), new Date(datePickerViewModel.getEndDate()));
    }

    @Override
    public void onErrorLoadDatePicker(Throwable throwable) {
        if (datePickerViewModel == null) {
            setDefaultDateViewModel();
        } else {
            datePickerPresenter.saveDateSetting(datePickerViewModel);
        }
        loadData();
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
        datePickerViewModel = DatePickerUtils.convertDatePickerFromIntent(intent);
    }

    @Override
    public void openDatePicker() {
        startActivityForResult(getDatePickerIntent(), DatePickerConstant.REQUEST_CODE_DATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        datePickerPresenter.detachView();
    }
}