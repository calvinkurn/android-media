package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.content.Intent;
import android.view.View;

import com.tokopedia.seller.base.view.fragment.BaseDatePickerFragment;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.common.datepicker.view.model.PeriodRangeModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticDateUtils;

/**
 * Created by nathan on 7/14/17.
 */

public abstract class GMStatisticBaseDatePickerFragment extends BaseDatePickerFragment {

    public static final boolean ALLOW_COMPARE_DATE = true;

    @Override
    public Intent getDatePickerIntent(DatePickerViewModel datePickerViewModel) {
        return GMStatisticDateUtils.getDatePickerIntent(getActivity(), datePickerViewModel, ALLOW_COMPARE_DATE );
    }

    @Override
    public DatePickerViewModel getDefaultDateViewModel() {
        return GMStatisticDateUtils.getDefaultDatePickerViewModel();
    }

    @Override
    protected void setDateLabelView(DatePickerViewModel datePickerViewModel) {
        super.setDateLabelView(datePickerViewModel);
        if (ALLOW_COMPARE_DATE && datePickerViewModel.isCompareDate()) {
            PeriodRangeModel periodRangeModel = GMStatisticDateUtils.getComparedDate(
                    datePickerViewModel.getStartDate(),
                    datePickerViewModel.getEndDate());
            dateLabelView.setComparedDate(periodRangeModel.getStartDate(), periodRangeModel.getEndDate());
            dateLabelView.setComparedDateVisibility(View.VISIBLE);
        } else {
            dateLabelView.setComparedDateVisibility(View.GONE);
        }
    }

}