package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.myetalase.MyEtalaseListServiceModel;
import com.tokopedia.seller.product.domain.model.MyEtalaseListDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class MyEtalaseServiceToDomainMapper implements Func1<MyEtalaseListServiceModel, MyEtalaseListDomainModel> {
    @Override
    public MyEtalaseListDomainModel call(MyEtalaseListServiceModel myEtalaseListServiceModel) {
        return null;
    }
}
