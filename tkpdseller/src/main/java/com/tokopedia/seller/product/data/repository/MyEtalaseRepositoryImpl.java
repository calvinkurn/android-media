package com.tokopedia.seller.product.data.repository;

import com.tokopedia.seller.product.data.source.MyEtalaseDataSource;
import com.tokopedia.seller.product.domain.MyEtalaseRepository;
import com.tokopedia.seller.product.domain.model.MyEtalaseListDomainModel;

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
    public Observable<MyEtalaseListDomainModel> fetchMyEtalase() {
        return myEtalaseDataSource.fetchMyEtalase();
    }
}
