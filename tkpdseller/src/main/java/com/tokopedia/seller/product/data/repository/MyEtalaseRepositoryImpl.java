package com.tokopedia.seller.product.data.repository;

import com.tokopedia.seller.product.data.source.MyEtalaseDataSource;
import com.tokopedia.seller.product.domain.MyEtalaseRepository;
import com.tokopedia.seller.product.domain.model.MyEtalaseDomainModel;

import java.util.List;

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
    public Observable<List<MyEtalaseDomainModel>> fetchMyEtalase() {
        return myEtalaseDataSource.fetchMyEtalase();
    }
}
