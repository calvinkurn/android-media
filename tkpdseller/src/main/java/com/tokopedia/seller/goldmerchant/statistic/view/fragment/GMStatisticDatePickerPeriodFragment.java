package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.datepicker.fragment.DatePickerPeriodFragment;

/**
 * Created by nathan on 7/12/17.
 */

public class GMStatisticDatePickerPeriodFragment extends DatePickerPeriodFragment {

    public interface Callback {

        void onDateSubmitted(int selectionPeriod, long startDate, long endDate,
                             long compareStartDate, long compareEndDate);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker_period, container, false);
    }
}
