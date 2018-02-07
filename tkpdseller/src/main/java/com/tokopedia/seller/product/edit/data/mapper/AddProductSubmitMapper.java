package com.tokopedia.seller.product.edit.data.mapper;

import com.tokopedia.seller.product.edit.data.source.cloud.model.ProductUploadResultModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductsubmit.AddProductSubmitResult;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductsubmit.AddProductSubmitServiceModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductSubmitMapper implements Func1<ProductUploadResultModel, AddProductDomainModel> {
    @Override
    public AddProductDomainModel call(ProductUploadResultModel serviceModel) {
        AddProductDomainModel domainModel = new AddProductDomainModel();
        domainModel.setProductId(serviceModel.getProductID());
        domainModel.setProductDesc(serviceModel.getShortDesc());
        domainModel.setProductName(serviceModel.getProductName());
        return domainModel;
    }
}
