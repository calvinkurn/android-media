package com.tokopedia.seller.product.picker.data.mapper;

import com.tokopedia.seller.product.picker.data.model.ProductListSellerModel;
import com.tokopedia.seller.product.picker.domain.model.ProductListSellerDomainModel;

import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 9/6/17.
 */

public class GetProductListSellingMapper implements Func1<ProductListSellerModel, ProductListSellerDomainModel> {
    @Override
    public ProductListSellerDomainModel call(ProductListSellerModel productListSellerModelResponse) {
        if(productListSellerModelResponse != null && productListSellerModelResponse.getData() != null
                && productListSellerModelResponse.getData().getList() != null) {
            return null;
        }else{
            throw new RuntimeException("Data tidak ada");
        }
    }
}
