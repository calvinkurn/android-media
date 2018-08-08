package com.tokopedia.seller.product.edit.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.seller.product.common.constant.ProductNetworkConstant;
import com.tokopedia.seller.product.edit.data.mapper.ImageProductUploadMapper;
import com.tokopedia.seller.product.edit.data.source.cloud.api.ImageUploadApi;
import com.tokopedia.seller.product.edit.data.source.cloud.model.AddProductPictureInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.ResultUploadImage;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductpicture.AddProductPictureServiceModel;
import com.tokopedia.seller.shopscore.data.common.GetData;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ImageProductUploadDataSourceCloud extends BaseImageUploadSourceCloud {

    private final ImageProductUploadMapper imageProductUploadMapper;
    private final OkHttpClient okHttpClient;
    private final Retrofit.Builder retrofitBuilder;

    @Inject
    public ImageProductUploadDataSourceCloud(@ApplicationContext Context context,
                                             @DefaultAuthWithErrorHandler OkHttpClient okHttpClient,
                                             ImageProductUploadMapper imageProductUploadMapper,
                                             Retrofit.Builder retrofitBuilder) {
        super(context);
        this.imageProductUploadMapper = imageProductUploadMapper;
        this.okHttpClient = okHttpClient;
        this.retrofitBuilder = retrofitBuilder;
    }

    public Observable<ResultUploadImage> uploadImage(String hostUrl, int serverId, String imagePath, int productId) {
        String urlUpload = ProductNetworkConstant.getUploadImageUrl(hostUrl);
        Retrofit retrofit = retrofitBuilder.baseUrl(urlUpload).client(okHttpClient).build();
        return retrofit.create(ImageUploadApi.class)
                .uploadImage(getParamsUploadImage(hostUrl, imagePath, String.valueOf(serverId), String.valueOf(productId)))
                .map(imageProductUploadMapper);
    }

    public Observable<AddProductPictureServiceModel> addProductPicture(AddProductPictureInputServiceModel serviceModel) {
        String urlUpload = ProductNetworkConstant.getUploadImageUrl(serviceModel.getHostUrl());
        Retrofit retrofit = retrofitBuilder.baseUrl(urlUpload).client(okHttpClient).build();
        return retrofit.create(ImageUploadApi.class)
                .addProductPicture(AuthUtil.generateParamsNetwork(context, serviceModel.generateMapParam()))
                .map(new GetData<AddProductPictureServiceModel>());
    }
}
