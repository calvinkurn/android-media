package com.tokopedia.seller.myproduct.network;

import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.seller.myproduct.network.apis.UploadImageProduct;

import retrofit2.Retrofit;

/**
 * @author noiz354 on 8/3/16.
 */
public class ProductActAfterService extends AuthService<UploadImageProduct> {
    public static final String WEB_SERVICE_V4_ACTION_UPLOAD_IMAGE_HELPER = "/web-service/v4/action/upload-image-helper/";
    String baseUrl;

    public ProductActAfterService(String baseUrl) {
        super(baseUrl);
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(UploadImageProduct.class);
    }

    @Override
    protected String getBaseUrl() {
        return baseUrl+WEB_SERVICE_V4_ACTION_UPLOAD_IMAGE_HELPER;
    }

    @Override
    public UploadImageProduct getApi() {
        return api;
    }
}
