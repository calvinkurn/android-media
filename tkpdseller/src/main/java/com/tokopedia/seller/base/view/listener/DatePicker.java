package com.tokopedia.seller.base.view.listener;

import android.content.Intent;

import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;

/**
 * Created by nathan on 7/18/17.
 */

public interface DatePicker extends BaseDatePicker {

    void loadDataByDate(DatePickerViewModel datePickerViewModel);

}
