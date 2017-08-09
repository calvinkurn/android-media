package com.tokopedia.seller.product.data.repository;

import com.tokopedia.seller.product.data.mapper.MyEtalaseServiceToDomainMapper;
import com.tokopedia.seller.product.data.source.MyEtalaseDataSource;
import com.tokopedia.seller.product.edit.domain.MyEtalaseRepository;
import com.tokopedia.seller.product.edit.domain.model.MyEtalaseDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class MyEtalaseRepositoryImpl implements MyEtalaseRepository {
    private final MyEtalaseDataSource myEtalaseDataSource;

    public MyEtalaseRepositoryImpl(MyEtalaseDataSource myEtalaseDataSource) {
        this.myEtalaseDataSource = myEtalaseDataSource;
    }

    @Override
    public Observable<MyEtalaseDomainModel> fetchMyEtalase(int page) {
        return myEtalaseDataSource.fetchMyEtalase(page)
                .map(new MyEtalaseServiceToDomainMapper());
    }

    @Override
    public Observable<Boolean> addNewEtalase(String newEtalaseName) {
        return myEtalaseDataSource.addNewEtalase(newEtalaseName);
    }
}
