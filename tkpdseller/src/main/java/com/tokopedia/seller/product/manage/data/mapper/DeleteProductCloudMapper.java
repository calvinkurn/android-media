package com.tokopedia.seller.product.manage.data.mapper;

import com.tokopedia.seller.product.manage.data.model.ResponseDeleteProductData;

import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class DeleteProductCloudMapper implements Func1<ResponseDeleteProductData, Boolean> {
    @Override
    public Boolean call(ResponseDeleteProductData responseDeleteProductData) {
        return responseDeleteProductData != null && responseDeleteProductData.getIs_success() == 1;
    }
}
