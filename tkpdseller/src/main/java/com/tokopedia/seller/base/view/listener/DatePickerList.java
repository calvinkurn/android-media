package com.tokopedia.seller.base.view.listener;

import com.tokopedia.product.common.model.DatePickerViewModel;

/**
 * Created by nathan on 7/18/17.
 */

public interface DatePickerList extends BaseDatePicker{

    void loadDataByDateAndPage(DatePickerViewModel datePickerViewModel, int page);
}
