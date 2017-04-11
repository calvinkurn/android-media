package com.tokopedia.seller.product.data.mapper;


import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.seller.product.data.source.cloud.model.UploadImageModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public class ImageProductUploadMapper implements Func1<Response<UploadImageModel>, UploadImageModel.Result> {

    @Inject
    public ImageProductUploadMapper() {
    }

    @Override
    public UploadImageModel.Result call(Response<UploadImageModel> uploadShopImageModelResponse) {
        if (uploadShopImageModelResponse.isSuccessful() && uploadShopImageModelResponse.body() != null
                && uploadShopImageModelResponse.body().getResult() != null) {
            return  uploadShopImageModelResponse.body().getResult();
        } else {
            return null;
        }
    }
}
