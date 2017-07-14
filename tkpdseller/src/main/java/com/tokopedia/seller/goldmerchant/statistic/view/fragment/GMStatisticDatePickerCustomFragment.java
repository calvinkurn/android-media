package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.common.datepicker.view.fragment.DatePickerCustomFragment;

/**
 * Created by nathan on 7/12/17.
 */

public class GMStatisticDatePickerCustomFragment extends DatePickerCustomFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gm_statistic_date_picker_custom, container, false);
        return view;
    }
}
