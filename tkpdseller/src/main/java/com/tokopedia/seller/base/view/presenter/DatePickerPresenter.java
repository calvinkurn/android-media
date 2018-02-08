package com.tokopedia.seller.base.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.base.view.listener.DatePickerView;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;

/**
 * @author sebastianuskh on 4/13/17.
 */

public abstract class DatePickerPresenter<T extends DatePickerView> extends BaseDaggerPresenter<T> {

    public abstract void fetchDatePickerSetting();

    public abstract void saveDateSetting(DatePickerViewModel datePickerViewModel);

    public abstract void clearDatePickerSetting();
}
