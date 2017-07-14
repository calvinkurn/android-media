package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.content.Intent;

import com.tokopedia.seller.base.view.fragment.BaseDatePickerFragment;

/**
 * Created by nathan on 7/14/17.
 */

public abstract class GMStatisticBaseDatePickerFragment extends BaseDatePickerFragment {

    @Override
    protected Intent getDatePickerIntent() {
        return null;
    }
}
