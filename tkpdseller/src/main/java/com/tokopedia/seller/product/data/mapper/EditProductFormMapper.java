package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.editproductform.EditProductFormServiceModel;
import com.tokopedia.seller.product.domain.model.EditProductFormDomainModel;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class EditProductFormMapper implements Func1<EditProductFormServiceModel, EditProductFormDomainModel> {
    @Inject
    public EditProductFormMapper() {
    }

    @Override
    public EditProductFormDomainModel call(EditProductFormServiceModel editProductFormServiceModel) {
        return null;
    }
}
