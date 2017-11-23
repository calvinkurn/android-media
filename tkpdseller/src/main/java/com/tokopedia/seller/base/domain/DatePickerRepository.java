package com.tokopedia.seller.base.domain;

import com.tokopedia.seller.base.domain.model.DatePickerDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface DatePickerRepository {

    Observable<Boolean> clearSetting();

    Observable<DatePickerDomainModel> fetchSetting();

    Observable<Boolean> saveSetting(DatePickerDomainModel datePickerDomainModel);
}
