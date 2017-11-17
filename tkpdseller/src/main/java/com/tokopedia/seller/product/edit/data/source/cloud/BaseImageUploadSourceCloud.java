package com.tokopedia.seller.product.edit.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.seller.product.common.constant.ProductNetworkConstant;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;

/**
 * Created by zulfikarrahman on 4/11/17.
 */

public abstract class BaseImageUploadSourceCloud {
    protected final Context context;

    public BaseImageUploadSourceCloud(@ApplicationContext Context context) {
        this.context = context;
    }


    public Map<String,RequestBody> getParamsUploadImage(String urlUploadImage, String pathFile, String serverIdUpload, String productId) {
        Map<String, RequestBody> paramsUploadImage = new TKPDMapParam<>();

        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, context,
                urlUploadImage).setIdentity().compileAllParam().finish();

        File file = new File(pathFile);

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.HASH));
        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody newAdd = RequestBody.create(MediaType.parse("text/plain"), ProductNetworkConstant.GOLANG_VALUE);
        RequestBody resolution = RequestBody.create(MediaType.parse("text/plain"), ProductNetworkConstant.RESOLUTION_DEFAULT_VALUE);
        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), serverIdUpload);
        RequestBody productIdBody = RequestBody.create(MediaType.parse("text/plain"), productId);

        paramsUploadImage.put(NetworkCalculator.USER_ID,userId);
        paramsUploadImage.put(NetworkCalculator.DEVICE_ID, deviceId);
        paramsUploadImage.put(NetworkCalculator.HASH, hash);
        paramsUploadImage.put(NetworkCalculator.DEVICE_TIME, deviceTime);
        paramsUploadImage.put(ProductNetworkConstant.LOGO_FILENAME_IMAGE_JPG, fileToUpload);
        paramsUploadImage.put(ProductNetworkConstant.SERVER_LANGUAGE, newAdd);
        paramsUploadImage.put(ProductNetworkConstant.RESOLUTION, resolution);
        paramsUploadImage.put(ProductNetworkConstant.SERVER_ID, serverId);
        if(productId !=null && !productId.isEmpty()){
            paramsUploadImage.put(ProductNetworkConstant.PRODUCT_ID, productIdBody);
        }

        return paramsUploadImage;
    }
}
