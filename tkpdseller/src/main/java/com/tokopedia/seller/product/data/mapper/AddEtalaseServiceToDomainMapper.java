package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.core.network.retrofit.exception.ResponseErrorListStringException;
import com.tokopedia.seller.product.data.source.cloud.model.addetalase.AddEtalaseServiceModel;

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
