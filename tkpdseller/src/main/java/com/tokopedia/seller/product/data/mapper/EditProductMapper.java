package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.EditProductServiceModel;
import com.tokopedia.seller.product.domain.model.EditProductDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/12/17.
 */

public class EditProductMapper implements Func1<EditProductServiceModel, EditProductDomainModel> {
    @Override
    public EditProductDomainModel call(EditProductServiceModel editProductServiceModel) {
        return null;
    }
}
