package com.tokopedia.seller.goldmerchant.statistic.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;

/**
 * @author sebastianuskh on 4/13/17.
 */

public interface DatePickerView extends CustomerView {

    void onSuccessLoadDatePicker(DatePickerViewModel datePickerViewModel);

    void onErrorLoadDatePicker(Throwable throwable);

    void onSuccessSaveDatePicker();

    void onErrorSaveDatePicker(Throwable throwable);

    void onSuccessResetDatePicker();

    void onErrorResetDatePicker(Throwable throwable);
}