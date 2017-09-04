package com.tokopedia.seller.product.edit.data.mapper;


import com.tokopedia.seller.product.edit.data.source.cloud.model.ResultUploadImage;
import com.tokopedia.seller.product.edit.data.source.cloud.model.UploadImageModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public class ImageProductUploadMapper implements Func1<Response<UploadImageModel>, ResultUploadImage> {

    @Inject
    public ImageProductUploadMapper() {
    }

    @Override
    public ResultUploadImage call(Response<UploadImageModel> uploadShopImageModelResponse) {
        if (uploadShopImageModelResponse.isSuccessful() && uploadShopImageModelResponse.body() != null
                && uploadShopImageModelResponse.body().getResult() != null) {
            return  uploadShopImageModelResponse.body().getResult();
        } else {
            return null;
        }
    }
}
