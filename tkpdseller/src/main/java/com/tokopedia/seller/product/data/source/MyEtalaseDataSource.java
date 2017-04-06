package com.tokopedia.seller.product.data.source;

import com.tokopedia.seller.product.data.mapper.AddEtalaseServiceToDomainMapper;
import com.tokopedia.seller.product.data.mapper.MyEtalaseServiceToDomainMapper;
import com.tokopedia.seller.product.data.source.cloud.MyEtalaseCloud;
import com.tokopedia.seller.product.domain.model.MyEtalaseDomainModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class MyEtalaseDataSource {
    private final MyEtalaseCloud myEtalaseCloud;

    @Inject
    public MyEtalaseDataSource(MyEtalaseCloud myEtalaseCloud) {
        this.myEtalaseCloud = myEtalaseCloud;
    }

    public Observable<List<MyEtalaseDomainModel>> fetchMyEtalase() {
        return myEtalaseCloud
                .fetchMyEtalaseList()
                .map(new MyEtalaseServiceToDomainMapper());
    }

    public Observable<Boolean> addNewEtalase(String newEtalaseName) {
        return myEtalaseCloud
                .addNewEtalase(newEtalaseName)
                .map(new AddEtalaseServiceToDomainMapper());
    }
}
