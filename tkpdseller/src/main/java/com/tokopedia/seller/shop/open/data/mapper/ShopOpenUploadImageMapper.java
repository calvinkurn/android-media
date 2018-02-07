package com.tokopedia.seller.shop.open.data.mapper;

import com.tokopedia.seller.shop.open.data.model.UploadShopImageModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public class ShopOpenUploadImageMapper implements Func1<Response<UploadShopImageModel>, UploadShopImageModel.Data> {

    @Inject
    public ShopOpenUploadImageMapper() {
    }

    @Override
    public UploadShopImageModel.Data call(Response<UploadShopImageModel> uploadShopImageModelResponse) {
        if (uploadShopImageModelResponse.isSuccessful() && uploadShopImageModelResponse.body() != null
                && uploadShopImageModelResponse.body().getData() != null) {
            return uploadShopImageModelResponse.body().getData();
        } else {
            return null;
        }
    }
}
