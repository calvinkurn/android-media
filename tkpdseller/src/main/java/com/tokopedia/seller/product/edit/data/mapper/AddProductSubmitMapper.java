package com.tokopedia.seller.product.edit.data.mapper;

import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductsubmit.AddProductSubmitResult;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductsubmit.AddProductSubmitServiceModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductSubmitMapper implements Func1<AddProductSubmitServiceModel, AddProductDomainModel> {
    @Override
    public AddProductDomainModel call(AddProductSubmitServiceModel serviceModel) {
        AddProductDomainModel domainModel = new AddProductDomainModel();
        AddProductSubmitResult result = serviceModel.getAddProductSubmitResult();
        domainModel.setProductId(result.getProductId());
        domainModel.setProductDesc(result.getProductDesc());
        domainModel.setProductEtalase(result.getProductEtalase());
        domainModel.setProductDest(result.getProductDest());
        domainModel.setProductName(result.getProductName());
        domainModel.setProductUrl(result.getProductUrl());
        domainModel.setProductPrimaryPic(result.getProductPrimaryPic());
        return domainModel;
    }
}
