package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.EditImageProductServiceModel;
import com.tokopedia.seller.product.domain.model.EditImageProductDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class EditProductImageMapper implements Func1<EditImageProductServiceModel, EditImageProductDomainModel> {
    @Override
    public EditImageProductDomainModel call(EditImageProductServiceModel serviceModel) {
        EditImageProductDomainModel domainModel = new EditImageProductDomainModel();
        return domainModel;
    }
}
