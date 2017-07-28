package com.tokopedia.seller.common.datepicker.view.model;

import android.support.annotation.Nullable;

import com.tokopedia.seller.base.domain.model.DatePickerDomainModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticDateUtils;

/**
 * Created by nathan on 7/13/17.
 */

public class DatePickerViewModel extends DatePickerDomainModel {

    public DatePickerViewModel() {
        super();
    }

    public DatePickerViewModel(DatePickerDomainModel datePickerDomainModel) {
        super(datePickerDomainModel);
    }

    public boolean equals(@Nullable DatePickerViewModel datePickerViewModel) {
        if (datePickerViewModel == null) {
            return false;
        }
        if (this.getStartDate() != datePickerViewModel.getStartDate()) {
            return false;
        }
        if (this.getEndDate() != datePickerViewModel.getEndDate()) {
            return false;
        }
        if (this.getDatePickerType() != datePickerViewModel.getDatePickerType()) {
            return false;
        }
        if (this.getDatePickerSelection() != datePickerViewModel.getDatePickerSelection()) {
            return false;
        }
        if (this.isCompareDate() != datePickerViewModel.isCompareDate()) {
            return false;
        }
        return true;
    }

}
