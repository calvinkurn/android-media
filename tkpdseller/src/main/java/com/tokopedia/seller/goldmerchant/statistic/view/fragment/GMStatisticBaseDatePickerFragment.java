package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.content.Intent;

import com.tokopedia.seller.base.view.fragment.BaseDatePickerFragment;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatatisticDateUtils;

/**
 * Created by nathan on 7/14/17.
 */

public abstract class GMStatisticBaseDatePickerFragment extends BaseDatePickerFragment {

    @Override
    public Intent getDatePickerIntent() {
        return GMStatatisticDateUtils.getDatePickerIntent(getActivity(), datePickerViewModel);
    }

    @Override
    public void setDefaultDateViewModel() {
        datePickerViewModel = GMStatatisticDateUtils.getDefaultDatePickerViewModel();
    }
}
