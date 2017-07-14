package com.tokopedia.seller.goldmerchant.statistic.view.activity;

import com.tokopedia.seller.lib.datepicker.DatePickerActivity;
import com.tokopedia.seller.lib.datepicker.fragment.DatePickerCustomFragment;
import com.tokopedia.seller.lib.datepicker.fragment.DatePickerPeriodFragment;

/**
 * Created by nathan on 7/11/17.
 */

public class GMStatisticDatePickerActivity extends DatePickerActivity {

    protected DatePickerPeriodFragment getDatePickerPeriodFragment() {
        DatePickerPeriodFragment datePickerPeriodFragment = DatePickerPeriodFragment.newInstance(selectionPeriod, periodRangeModelList);
        datePickerPeriodFragment.setCallback(this);
        return datePickerPeriodFragment;
    }

    protected DatePickerCustomFragment getDatePickerCustomFragment() {
        DatePickerCustomFragment datePickerCustomFragment = DatePickerCustomFragment.newInstance(startDate, endDate, minStartDate, maxStartDate, maxDateRange);
        datePickerCustomFragment.setCallback(this);
        return datePickerCustomFragment;
    }
}
