package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.exception.FailedToAddEtalaseException;
import com.tokopedia.seller.product.data.source.cloud.model.addetalase.AddEtalaseServiceModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/6/17.
 */

public class AddEtalaseServiceToDomainMapper implements Func1<AddEtalaseServiceModel, Boolean> {
    @Override
    public Boolean call(AddEtalaseServiceModel serviceModel) {
        if (serviceModel.getData().getIsSuccess() == 0){
            if (serviceModel.getMessageError() != null && !serviceModel.getMessageError().isEmpty()){
                throw new FailedToAddEtalaseException(serviceModel.getMessageError().get(0));
            } else {
                throw new RuntimeException("Unknown Error");
            }
        }
        return true;
    }
}
