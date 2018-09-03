package com.tokopedia.seller.base.view.listener;

import com.tokopedia.product.manage.item.common.model.DatePickerViewModel;

/**
 * Created by nathan on 7/18/17.
 */

public interface DatePicker extends BaseDatePicker {

    void loadDataByDate(DatePickerViewModel datePickerViewModel);

}
