package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.content.Intent;
import android.view.View;

import com.tokopedia.seller.base.view.fragment.BaseDatePickerFragment;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.common.datepicker.view.model.PeriodRangeModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatatisticDateUtils;

/**
 * Created by nathan on 7/14/17.
 */

public abstract class GMStatisticBaseDatePickerFragment extends BaseDatePickerFragment {

    private long comparedStartDate;
    private long comparedEndDate;

    protected long getComparedStartDate() {
        return comparedStartDate;
    }

    protected long getComparedEndDate() {
        return comparedEndDate;
    }

    @Override
    public void onSuccessLoadDatePicker(DatePickerViewModel datePickerFromDatabase) {
        // Check if date compared changed
        if (datePickerViewModel != null && isComparedDate() &&
                (datePickerViewModel.isCompareDate() != datePickerFromDatabase.isCompareDate()) ) {
            datePickerPresenter.saveDateSetting(datePickerViewModel);
            loadData();
        } else {
            super.onSuccessLoadDatePicker(datePickerFromDatabase);
        }
    }

    @Override
    public void loadData() {
        super.loadData();
        if (isComparedDate() && datePickerViewModel.isCompareDate()) {
            PeriodRangeModel comparedPeriodRangeModel = GMStatatisticDateUtils.getComparedDate(datePickerViewModel.getStartDate(), datePickerViewModel.getEndDate());
            comparedStartDate = comparedPeriodRangeModel.getStartDate();
            comparedEndDate = comparedPeriodRangeModel.getEndDate();
            dateLabelView.setComparedDate(comparedStartDate, comparedEndDate);
            dateLabelView.setComparedDateVisibility(View.VISIBLE);
        } else {
            dateLabelView.setComparedDateVisibility(View.GONE);
        }
    }

    @Override
    protected void onDateSelected(Intent intent) {
        super.onDateSelected(intent);
        if (isComparedDate()) {
            datePickerViewModel.setCompareDate(intent.getBooleanExtra(DatePickerConstant.EXTRA_COMPARE_DATE, false));
        }
    }

    @Override
    public Intent getDatePickerIntent() {
        return GMStatatisticDateUtils.getDatePickerIntent(getActivity(), datePickerViewModel, isComparedDate());
    }

    @Override
    public void setDefaultDateViewModel() {
        datePickerViewModel = GMStatatisticDateUtils.getDefaultDatePickerViewModel();
    }

    public boolean isComparedDate() {
        return false;
    }
}