package com.tokopedia.seller.base.data.mapper;

import com.tokopedia.seller.base.data.source.cache.model.DatePickerCacheModel;
import com.tokopedia.seller.base.domain.model.DatePickerDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class DatePickerMapper implements Func1<DatePickerCacheModel, DatePickerDomainModel> {

    @Override
    public DatePickerDomainModel call(DatePickerCacheModel datePickerCacheModel) {
        return mapDomainModels(datePickerCacheModel);
    }

    public static DatePickerDomainModel mapDomainModels(DatePickerCacheModel datePickerCacheModel) {
        return datePickerCacheModel;
    }

}
