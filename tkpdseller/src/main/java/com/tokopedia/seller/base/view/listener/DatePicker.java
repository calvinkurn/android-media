package com.tokopedia.seller.base.view.listener;

import android.content.Intent;

/**
 * Created by nathan on 7/18/17.
 */

public interface DatePicker {

    Intent getDatePickerIntent();

    void loadData();

    void openDatePicker();

    void setDefaultDateViewModel();
}
