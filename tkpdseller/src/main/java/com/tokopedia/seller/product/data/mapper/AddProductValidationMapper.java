package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.AddProductValidationServiceModel;
import com.tokopedia.seller.product.domain.model.AddProductValidationDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductValidationMapper implements Func1<AddProductValidationServiceModel, AddProductValidationDomainModel> {
    @Override
    public AddProductValidationDomainModel call(AddProductValidationServiceModel addProductValidationServiceModel) {
        return null;
    }
}
