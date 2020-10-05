package com.tokopedia.seller.product.etalase.data.source;

import com.tokopedia.seller.manageitem.data.cloud.model.etalase.MyEtalaseListServiceModel;
import com.tokopedia.seller.product.etalase.data.mapper.AddEtalaseServiceToDomainMapper;
import com.tokopedia.seller.product.etalase.data.source.cloud.MyEtalaseCloud;

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

    public Observable<MyEtalaseListServiceModel> fetchMyEtalase(int page) {
        return myEtalaseCloud
                .fetchMyEtalaseList(page);
    }

    public Observable<Boolean> addNewEtalase(String newEtalaseName) {
        return myEtalaseCloud
                .addNewEtalase(newEtalaseName)
                .map(new AddEtalaseServiceToDomainMapper());
    }
}
