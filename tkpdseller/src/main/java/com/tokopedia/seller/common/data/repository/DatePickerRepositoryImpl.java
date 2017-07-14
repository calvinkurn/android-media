package com.tokopedia.seller.common.data.repository;

import com.tokopedia.seller.common.data.source.DatePickerDataSource;
import com.tokopedia.seller.common.domain.DatePickerRepository;
import com.tokopedia.seller.common.domain.model.DatePickerDomainModel;
import com.tokopedia.seller.product.data.source.db.model.ProductDraftDataBase;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.draft.data.mapper.ProductDraftMapper;
import com.tokopedia.seller.product.draft.data.source.ProductDraftDataSource;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class DatePickerRepositoryImpl implements DatePickerRepository {

    private final DatePickerDataSource datePickerDataSource;

    public DatePickerRepositoryImpl(DatePickerDataSource datePickerDataSource) {
        this.datePickerDataSource = datePickerDataSource;
    }

    @Override
    public Observable<DatePickerDomainModel> fetchSetting() {
        return datePickerDataSource.getData();
    }

    @Override
    public Observable<Boolean> saveSetting(DatePickerDomainModel datePickerDomainModel) {
        return datePickerDataSource.saveData(datePickerDomainModel);
    }

    @Override
    public Observable<Boolean> clearSetting() {
        return datePickerDataSource.clearData();
    }
}