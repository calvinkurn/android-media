package com.tokopedia.seller.product.etalase.data.mapper;

import com.tokopedia.seller.product.etalase.data.source.cloud.model.AddEtalaseServiceModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/6/17.
 */

public class AddEtalaseServiceToDomainMapper implements Func1<AddEtalaseServiceModel, Boolean> {
    @Override
    public Boolean call(AddEtalaseServiceModel serviceModel) {
        return true;
    }
}
