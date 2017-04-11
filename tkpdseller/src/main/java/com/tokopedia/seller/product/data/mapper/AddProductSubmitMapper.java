package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.AddProductSubmitServiceModel;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductSubmitMapper implements Func1<AddProductSubmitServiceModel, AddProductDomainModel> {
    @Override
    public AddProductDomainModel call(AddProductSubmitServiceModel addProductSubmitServiceModel) {
        return null;
    }
}
