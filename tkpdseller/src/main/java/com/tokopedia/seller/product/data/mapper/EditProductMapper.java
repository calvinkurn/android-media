package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.editproduct.EditProductServiceModel;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/12/17.
 */

public class EditProductMapper implements Func1<EditProductServiceModel, AddProductDomainModel> {
    @Override
    public AddProductDomainModel call(EditProductServiceModel editProductServiceModel) {
        return null;
    }
}
