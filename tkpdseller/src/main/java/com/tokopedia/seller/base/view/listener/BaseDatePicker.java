package com.tokopedia.seller.base.view.listener;

import android.content.Intent;

import com.tokopedia.product.common.model.DatePickerViewModel;

/**
 * Created by nathan on 7/18/17.
 */

public interface BaseDatePicker {
    Intent getDatePickerIntent(DatePickerViewModel datePickerViewModel);

    void openDatePicker();

    DatePickerViewModel getDefaultDateViewModel();

    void reloadDataForDate();
}
