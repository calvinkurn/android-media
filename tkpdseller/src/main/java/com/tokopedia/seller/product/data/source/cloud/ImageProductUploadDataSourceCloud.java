package com.tokopedia.seller.product.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.seller.product.constant.ProductNetworkConstant;
import com.tokopedia.seller.product.data.mapper.ImageProductUploadMapper;
import com.tokopedia.seller.product.data.source.cloud.api.ImageUploadApi;
import com.tokopedia.seller.product.data.source.cloud.model.GenerateHost;
import com.tokopedia.seller.product.data.source.cloud.model.ResultUploadImage;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ImageProductUploadDataSourceCloud extends BaseImageUploadSourceCloud {

    private final ImageProductUploadMapper imageProductUploadMapper;

    @Inject
    public ImageProductUploadDataSourceCloud(@ActivityContext Context context, ImageProductUploadMapper imageProductUploadMapper) {
        super(context);
        this.imageProductUploadMapper = imageProductUploadMapper;
    }

    public Observable<ResultUploadImage> uploadImage(GenerateHost generateHost, String pathFileImage) {
        String urlUploadImage = ProductNetworkConstant.getUploadImageUrl(generateHost.getUploadHost());
        return RetrofitUtils.createRetrofit(urlUploadImage)
                .create(ImageUploadApi.class)
                .uploadImage(getParamsUploadImage(urlUploadImage, pathFileImage, generateHost.getServerId()))
                .map(imageProductUploadMapper);
    }
}
