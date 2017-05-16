package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.editproduct.EditProductServiceModel;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/12/17.
 */

public class EditProductMapper implements Func1<EditProductServiceModel, Boolean> {
    @Override
    public Boolean call(EditProductServiceModel editProductServiceModel) {
        if (editProductServiceModel.getData().getIsSuccess() == 1) {
            return true;
        } else {
            throw new RuntimeException("Edit Product Failed");
        }
    }
}
