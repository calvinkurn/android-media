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

    @Override
    public Intent getDatePickerIntent(DatePickerViewModel datePickerViewModel) {
        return GMStatisticDateUtils.getDatePickerIntent(getActivity(), datePickerViewModel, allowComparedDate());
    }

    @Override
    public DatePickerViewModel getDefaultDateViewModel() {
        return GMStatisticDateUtils.getDefaultDatePickerViewModel();
    }

}